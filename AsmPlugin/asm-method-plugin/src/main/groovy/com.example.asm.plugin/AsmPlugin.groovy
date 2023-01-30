package com.example.asm.plugin

import com.android.annotations.NonNull
import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import com.example.asm.plugin.Utils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

class AsmPlugin extends Transform implements Plugin<Project> {

    @Override
    void apply(Project project) {
        //registerTransform
        println '-------register transform---------'
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    String getName() {
        return "AsmPlugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(@NonNull TransformInvocation transformInvocation) {
        println '--------------- AsmPlugin visit start --------------- '
        def startTime = System.currentTimeMillis()
        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (outputProvider != null)
            outputProvider.deleteAll()
        inputs.each { TransformInput input ->
            // directoryInputs
            input.directoryInputs.each { DirectoryInput directoryInput ->
                def preClassNamePath = directoryInput.file.absolutePath
                createNewClass(preClassNamePath)
                handleDirectoryInput(directoryInput, outputProvider)
            }

            // jarInputs
            input.jarInputs.each { JarInput jarInput ->
                handleJarInputs(jarInput, outputProvider)
            }
        }
        def cost = (System.currentTimeMillis() - startTime) / 1000
        println '--------------- AsmPlugin visit end --------------- '
        println "AsmPLugin cost ： $cost s"
    }

    /**
     * handle directory class
     */
    static void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        def rootPath = System.getProperty("user.dir")
        def directory = new File(rootPath)
        def xmlPath = getXmlFilePath(directory)
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                if (checkClassFile(name)) {
                    //println '----------- deal with "class" file <' + name + '> -----------'
                    ClassReader classReader = new ClassReader(file.bytes)
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new AsmClassVisitor(classWriter, xmlPath)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    FileOutputStream fos = new FileOutputStream(
                            file.parentFile.absolutePath + File.separator + name)
                    fos.write(code)
                    fos.close()
                }
            }
        }

        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    /**
     * handle Jar class
     */
    static void handleJarInputs(JarInput jarInput, TransformOutputProvider outputProvider) {
        def rootPath = System.getProperty("user.dir")
        def directory = new File(rootPath)
        def xmlPath = getXmlFilePath(directory)
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            if (tmpFile.exists()) {
                tmpFile.delete()
            }
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement()
                String entryName = jarEntry.getName()
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                if (checkClassFile(entryName)) {
                    //println '----------- deal with "jar" class file <' + entryName + '> -----------'
                    jarOutputStream.putNextEntry(zipEntry)
                    ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream))
                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    ClassVisitor cv = new AsmClassVisitor(classWriter, xmlPath)
                    classReader.accept(cv, EXPAND_FRAMES)
                    byte[] code = classWriter.toByteArray()
                    jarOutputStream.write(code)
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            jarFile.close()
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

    /**
     * check if the class file need to be handled
     * @param fileName
     * @return
     */
    static boolean checkClassFile(String name) {
        return (name.endsWith(".class") && !name.startsWith("R\$")
                && !"R.class".equals(name) && !"BuildConfig.class".equals(name) && !name.startsWith("MethodVisitor") && !name.startsWith("RealtimeCoverage"));
//                && "android/support/v4/app/FragmentActivity.class".equals(name))
    }

    static void createNewClass(String preClassNamePath) {
        byte[] code = Utils.generateCrashHandler()
        FileOutputStream fos = new FileOutputStream(preClassNamePath + File.separator + "CrashHandler.class")
        fos.write(code)
        fos.close()

        byte[] code1 = Utils.generateMethodVisitor()
        FileOutputStream fos1 = new FileOutputStream(preClassNamePath + File.separator + "MethodVisitor.class")
        fos1.write(code1)
        fos1.close()

        byte[] code2 = Utils.generateMyTimerTask()
        FileOutputStream fos2 = new FileOutputStream(preClassNamePath + File.separator + "MyTimerTask.class")
        fos2.write(code2)
        fos2.close()

        byte[] code3 = Utils.generateRealtimeCoverage()
        FileOutputStream fos3 = new FileOutputStream(preClassNamePath + File.separator + "RealtimeCoverage.class")
        fos3.write(code3)
        fos3.close()
    }

    static String getXmlFilePath(File directory) {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
//                System.out.println(file.getAbsolutePath());
                if (file.getName().equals("AndroidManifest.xml") && file.getAbsolutePath().contains("src") && file.getAbsolutePath().contains("main")) {
                    return file.getAbsolutePath();
                }
            } else if (file.isDirectory()) {
                String result = getXmlFilePath(file);
                if (!result.equals("not found")) {
                    return result;
                }
            }
        }
        return "not found";
    }
}