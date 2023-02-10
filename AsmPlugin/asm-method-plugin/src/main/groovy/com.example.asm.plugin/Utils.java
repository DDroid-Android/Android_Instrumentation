package com.example.asm.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Utils implements Opcodes {

    public static byte[] generateCrashHandler() throws Exception {

        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "realtimecoverage/CrashHandler", null, "java/lang/Object", new String[] {"java/lang/Thread$UncaughtExceptionHandler"});

        classWriter.visitInnerClass("java/lang/Thread$UncaughtExceptionHandler", "java/lang/Thread", "UncaughtExceptionHandler", ACC_PUBLIC | ACC_STATIC | ACC_ABSTRACT | ACC_INTERFACE);

        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_STATIC, "collectedMethods", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/String;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE, "mContext", "Landroid/content/Context;", null, null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_STATIC | ACC_VOLATILE, "mCrashHandler", "Lrealtimecoverage/CrashHandler;", null, null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE, "mDefaultHandler", "Ljava/lang/Thread$UncaughtExceptionHandler;", null, null);
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PRIVATE, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "getInstance", "()Lrealtimecoverage/CrashHandler;", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, null);
            Label label3 = new Label();
            methodVisitor.visitTryCatchBlock(label2, label3, label2, null);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/CrashHandler", "mCrashHandler", "Lrealtimecoverage/CrashHandler;");
            Label label4 = new Label();
            methodVisitor.visitJumpInsn(IFNONNULL, label4);
            methodVisitor.visitLdcInsn(Type.getType("Lrealtimecoverage/CrashHandler;"));
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ASTORE, 0);
            methodVisitor.visitInsn(MONITORENTER);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/CrashHandler", "mCrashHandler", "Lrealtimecoverage/CrashHandler;");
            Label label5 = new Label();
            methodVisitor.visitJumpInsn(IFNONNULL, label5);
            methodVisitor.visitTypeInsn(NEW, "realtimecoverage/CrashHandler");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "realtimecoverage/CrashHandler", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTSTATIC, "realtimecoverage/CrashHandler", "mCrashHandler", "Lrealtimecoverage/CrashHandler;");
            methodVisitor.visitLabel(label5);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitInsn(MONITOREXIT);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitJumpInsn(GOTO, label4);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitVarInsn(ASTORE, 1);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitInsn(MONITOREXIT);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInsn(ATHROW);
            methodVisitor.visitLabel(label4);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/CrashHandler", "mCrashHandler", "Lrealtimecoverage/CrashHandler;");
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitMaxs(2, 2);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "init", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "getDefaultUncaughtExceptionHandler", "()Ljava/lang/Thread$UncaughtExceptionHandler;", false);
            methodVisitor.visitFieldInsn(PUTFIELD, "realtimecoverage/CrashHandler", "mDefaultHandler", "Ljava/lang/Thread$UncaughtExceptionHandler;");
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "setDefaultUncaughtExceptionHandler", "(Ljava/lang/Thread$UncaughtExceptionHandler;)V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "uncaughtException", "(Ljava/lang/Thread;Ljava/lang/Throwable;)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MethodVisitor", "visitedMethods", "Ljava/util/concurrent/BlockingQueue;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/concurrent/BlockingQueue", "isEmpty", "()Z", true);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(IFNE, label3);
            methodVisitor.visitLdcInsn("Themis");
            methodVisitor.visitLdcInsn("The blocking queue is not empty!");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/CrashHandler", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "clear", "()V", true);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MethodVisitor", "visitedMethods", "Ljava/util/concurrent/BlockingQueue;");
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/CrashHandler", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/concurrent/BlockingQueue", "drainTo", "(Ljava/util/Collection;)I", true);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/os/Environment", "getExternalStorageDirectory", "()Ljava/io/File;", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsolutePath", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitLdcInsn("/realtimecoverage");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "mkdirs", "()Z", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitLdcInsn("coverage.txt");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/io/File;Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 5);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitTypeInsn(NEW, "java/io/BufferedWriter");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/io/OutputStreamWriter");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/io/FileOutputStream");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/FileOutputStream", "<init>", "(Ljava/io/File;Z)V", false);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/OutputStreamWriter", "<init>", "(Ljava/io/OutputStream;)V", false);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedWriter", "<init>", "(Ljava/io/Writer;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 6);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/CrashHandler", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true);
            methodVisitor.visitVarInsn(ASTORE, 7);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitVarInsn(ALOAD, 7);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
            Label label5 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, label5);
            methodVisitor.visitVarInsn(ALOAD, 7);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/String");
            methodVisitor.visitVarInsn(ASTORE, 8);
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ALOAD, 8);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitLdcInsn("\n");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "write", "(Ljava/lang/String;)V", false);
            methodVisitor.visitJumpInsn(GOTO, label4);
            methodVisitor.visitLabel(label5);
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "close", "()V", false);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitVarInsn(ASTORE, 7);
            methodVisitor.visitLdcInsn("Error: ");
            methodVisitor.visitVarInsn(ALOAD, 7);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "realtimecoverage/CrashHandler", "mDefaultHandler", "Ljava/lang/Thread$UncaughtExceptionHandler;");
            Label label6 = new Label();
            methodVisitor.visitJumpInsn(IFNULL, label6);
            methodVisitor.visitLdcInsn("uncaughtException");
            methodVisitor.visitLdcInsn("System Exception Handling...");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "realtimecoverage/CrashHandler", "mDefaultHandler", "Ljava/lang/Thread$UncaughtExceptionHandler;");
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/lang/Thread$UncaughtExceptionHandler", "uncaughtException", "(Ljava/lang/Thread;Ljava/lang/Throwable;)V", true);
            Label label7 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label7);
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLdcInsn("uncaughtException");
            methodVisitor.visitLdcInsn("Custom Exception Handling...");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/os/Process", "myPid", "()I", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/os/Process", "killProcess", "(I)V", false);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "exit", "(I)V", false);
            methodVisitor.visitLabel(label7);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(8, 9);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTSTATIC, "realtimecoverage/CrashHandler", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }

    public static byte[] generateMethodVisitor() throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "realtimecoverage/MethodVisitor", null, "java/lang/Object", null);

        {
            fieldVisitor = classWriter.visitField(ACC_PUBLIC | ACC_STATIC, "visitedMethods", "Ljava/util/concurrent/BlockingQueue;", "Ljava/util/concurrent/BlockingQueue<Ljava/lang/String;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "visit", "(Ljava/lang/String;Ljava/lang/String;)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MethodVisitor", "visitedMethods", "Ljava/util/concurrent/BlockingQueue;");
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitLdcInsn("/");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/concurrent/BlockingQueue", "put", "(Ljava/lang/Object;)V", true);
            methodVisitor.visitLabel(label1);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitVarInsn(ASTORE, 2);
            methodVisitor.visitLdcInsn("Error: ");
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(3, 3);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "visitFinish", "(Ljava/lang/String;Ljava/lang/String;)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MethodVisitor", "visitedMethods", "Ljava/util/concurrent/BlockingQueue;");
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            methodVisitor.visitLdcInsn("[");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitLdcInsn("/");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitLdcInsn("]");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/concurrent/BlockingQueue", "put", "(Ljava/lang/Object;)V", true);
            methodVisitor.visitLabel(label1);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label3);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitVarInsn(ASTORE, 2);
            methodVisitor.visitLdcInsn("Error: ");
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(3, 3);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "tearDown", "()V", null, new String[] {"java/lang/InterruptedException"});
            methodVisitor.visitCode();
            methodVisitor.visitLdcInsn("hahaha");
            methodVisitor.visitLdcInsn("dengdai");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitLdcInsn(new Long(5000L));
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep", "(J)V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, "java/util/concurrent/LinkedBlockingQueue");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/concurrent/LinkedBlockingQueue", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTSTATIC, "realtimecoverage/MethodVisitor", "visitedMethods", "Ljava/util/concurrent/BlockingQueue;");
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }

    public static byte[] generateMyTimerTask() throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_7, ACC_PUBLIC | ACC_SUPER, "realtimecoverage/MyTimerTask", null, "java/util/TimerTask", null);

        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_STATIC, "collectedMethods", "Ljava/util/List;", "Ljava/util/List<Ljava/lang/String;>;", null);
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/TimerTask", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitMethodInsn(INVOKESTATIC, "realtimecoverage/MyTimerTask", "saveFile", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PRIVATE | ACC_STATIC, "saveFile", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MyTimerTask", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "clear", "()V", true);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MethodVisitor", "visitedMethods", "Ljava/util/concurrent/BlockingQueue;");
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MyTimerTask", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/concurrent/BlockingQueue", "drainTo", "(Ljava/util/Collection;)I", true);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MyTimerTask", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "isEmpty", "()Z", true);
            Label label3 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, label3);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            methodVisitor.visitVarInsn(LSTORE, 0);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/os/Environment", "getExternalStorageDirectory", "()Ljava/io/File;", false);
            methodVisitor.visitVarInsn(ASTORE, 2);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsolutePath", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitLdcInsn("/realtimecoverage");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "mkdirs", "()Z", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitTypeInsn(NEW, "java/io/File");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitLdcInsn("coverage.txt");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/io/File;Ljava/lang/String;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitTypeInsn(NEW, "java/io/BufferedWriter");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/io/OutputStreamWriter");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitTypeInsn(NEW, "java/io/FileOutputStream");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/FileOutputStream", "<init>", "(Ljava/io/File;Z)V", false);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/OutputStreamWriter", "<init>", "(Ljava/io/OutputStream;)V", false);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedWriter", "<init>", "(Ljava/io/Writer;)V", false);
            methodVisitor.visitVarInsn(ASTORE, 5);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/MyTimerTask", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true);
            methodVisitor.visitVarInsn(ASTORE, 6);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
            Label label5 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, label5);
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/String");
            methodVisitor.visitVarInsn(ASTORE, 7);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitTypeInsn(NEW, "java/lang/StringBuilder");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ALOAD, 7);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitLdcInsn("\n");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "write", "(Ljava/lang/String;)V", false);
            methodVisitor.visitJumpInsn(GOTO, label4);
            methodVisitor.visitLabel(label5);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "close", "()V", false);
            methodVisitor.visitLabel(label1);
            Label label6 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label6);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitVarInsn(ASTORE, 6);
            methodVisitor.visitLdcInsn("Error: ");
            methodVisitor.visitVarInsn(ALOAD, 6);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "toString", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitLabel(label6);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(8, 8);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTSTATIC, "realtimecoverage/MyTimerTask", "collectedMethods", "Ljava/util/List;");
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }

    public static byte[] generateRealtimeCoverage() throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_7, ACC_PUBLIC | ACC_SUPER, "realtimecoverage/RealtimeCoverage", null, "java/lang/Object", null);

        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_STATIC, "timer", "Ljava/util/Timer;", null, null);
            fieldVisitor.visitEnd();
        }
        {
            fieldVisitor = classWriter.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "INTERVAL", "I", null, new Integer(1000));
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "init", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, "realtimecoverage/MyTimerTask");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "realtimecoverage/MyTimerTask", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 0);
            methodVisitor.visitFieldInsn(GETSTATIC, "realtimecoverage/RealtimeCoverage", "timer", "Ljava/util/Timer;");
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitInsn(LCONST_0);
            methodVisitor.visitLdcInsn(new Long(1000L));
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/Timer", "schedule", "(Ljava/util/TimerTask;JJ)V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(6, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, "java/util/Timer");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/Timer", "<init>", "()V", false);
            methodVisitor.visitFieldInsn(PUTSTATIC, "realtimecoverage/RealtimeCoverage", "timer", "Ljava/util/Timer;");
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        return classWriter.toByteArray();
    }

}
