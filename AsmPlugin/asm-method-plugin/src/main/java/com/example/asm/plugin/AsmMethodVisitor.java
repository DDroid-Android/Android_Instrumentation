package com.example.asm.plugin;

import com.android.bundle.Commands;

import org.objectweb.asm.*;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.util.HashSet;
import java.util.Set;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class AsmMethodVisitor extends MethodVisitor {
    private String className;
    private String methodName;
    private String desc;
    private int flag;
    private String signature;
    private String packageName;
    Label newStart = new Label();
    private int lineNumber = -1;
    private Set<String> methodSet = new HashSet() {{
        add("onMenuItemClick");
        add("onNavigationItemSelected");
        add("onOptionsItemSelected");
        add("onActionItemClicked");
        add("onClick");
        add("onLongClick");
        add("onDrawerOpened");
        add("onListItemClick");
        add("onItemClick");
        add("onItemLongClick");
        add("onPreferenceChange");
        add("onPreferenceClick");
        add("afterTextChanged");
        add("onSharedPreferenceChanged");
    }};
    private Set<String> lifecycleSet = new HashSet() {{
        add("onCreate");
    }};

    public AsmMethodVisitor(MethodVisitor mv, String className, String methodName, String desc, int flag, String signature, String packageName) {
        super(Opcodes.ASM6, mv);
        this.className = className;
        this.methodName = methodName;
        this.desc = desc;
        this.flag = flag;
        this.signature = signature;
        this.packageName = packageName;
    }

    public void insertTime(){
        mv.visitTypeInsn(Opcodes.NEW, "java/text/SimpleDateFormat");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("HH:mm:ss");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/text/SimpleDateFormat", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/text/SimpleDateFormat", "format", "(Ljava/lang/Object;)Ljava/lang/String;", false);
        mv.visitLdcInsn("");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        visitLabel(newStart);

        // if this method is "onCreate" of Main Activity
        if(flag == 2 && methodName.equals("onCreate") && className.startsWith(packageName)) {
            System.out.println("Initializing Blocking Queue...");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/RealtimeCoverage", "init", "()V", false);
        }

        // if this method is a lifecycle method of an Activity
        if(flag != 0 && lifecycleSet.contains(methodName) && className.startsWith(packageName)) {
            System.out.println("Instrumenting：" + className + "/" + methodName + ", desc:" + desc);
            insertTime();
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }

        if(methodSet.contains(methodName) && className.startsWith(packageName)) {
            String parameterString = desc.substring(desc.indexOf("(") + 1, desc.lastIndexOf(")"));
            String[] parameters = parameterString.split(";");
            System.out.println("Instrumenting：" + className + "/" + methodName + ", desc:" + desc);
            insertTime();
            for (int i = 0; i < parameters.length; i++) {
                if(parameters[i].equals("Landroid/text/Editable")) {
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
                    mv.visitLdcInsn("");
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                }
                else if(parameters[i].equals("Ljava/lang/String") && methodName.equals("onSharedPreferenceChanged")) {
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    Label label0 = new Label();
                    mv.visitJumpInsn(Opcodes.IFNULL, label0);
                    mv.visitLdcInsn("");
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                    mv.visitLabel(label0);
                }
                else if(parameters[i].equals("Landroid/widget/AdapterView")) {
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitVarInsn(Opcodes.ILOAD, i+3);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/widget/AdapterView", "getItemAtPosition", "(I)Ljava/lang/Object;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
                    mv.visitLdcInsn("");
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                }
                else if(parameters[i].equals("Landroid/view/MenuItem")) {
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "android/view/MenuItem", "getItemId", "()I", true);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;", false);
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "android/view/MenuItem", "getTitle", "()Ljava/lang/CharSequence;", true);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                }
                else if(parameters[i].equals("Landroid/view/View")) {
                    mv.visitInsn(Opcodes.ICONST_2);
                    mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
                    mv.visitVarInsn(Opcodes.ASTORE, parameters.length + 1);
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitVarInsn(Opcodes.ALOAD, parameters.length + 1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/view/View", "getLocationInWindow", "([I)V", false);
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/view/View", "getId", "()I", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;", false);
                    mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/view/View", "getContext", "()Landroid/content/Context;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    mv.visitLdcInsn("/(");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    mv.visitVarInsn(Opcodes.ALOAD, parameters.length + 1);
                    mv.visitInsn(Opcodes.ICONST_0);
                    mv.visitInsn(Opcodes.IALOAD);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
                    mv.visitLdcInsn(",");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    mv.visitVarInsn(Opcodes.ALOAD, parameters.length + 1);
                    mv.visitInsn(Opcodes.ICONST_1);
                    mv.visitInsn(Opcodes.IALOAD);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
                    mv.visitLdcInsn(")");
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                }
                else if(parameters[i].equals("Landroid/preference/Preference")) {
                    if(methodName.equals("onPreferenceChange")){
                        mv.visitVarInsn(Opcodes.ALOAD, i+1);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/preference/Preference", "getTitle", "()Ljava/lang/CharSequence;", false);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false);
                        mv.visitVarInsn(Opcodes.ALOAD, i+2);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                    }
                    else {
                        mv.visitVarInsn(Opcodes.ALOAD, i+1);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/preference/Preference", "getTitle", "()Ljava/lang/CharSequence;", false);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false);
                        mv.visitVarInsn(Opcodes.ALOAD, i+1);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/preference/Preference", "getContext", "()Landroid/content/Context;", false);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                    }
                }
                else if(parameters[i].equals("Landroid/content/DialogInterface")){
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
                    mv.visitVarInsn(Opcodes.ILOAD, i+2);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                }
                else if(parameters[i].equals("Lcom/afollestad/materialdialogs/MaterialDialog")){
                    mv.visitVarInsn(Opcodes.ALOAD, i+1);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/afollestad/materialdialogs/MaterialDialog", "getView", "()Landroid/view/View;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/view/View", "getContext", "()Landroid/content/Context;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
                    mv.visitVarInsn(Opcodes.ALOAD, i+2);
                    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/afollestad/materialdialogs/DialogAction", "name", "()Ljava/lang/String;", false);
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
                }
            }
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);

        // Main Activity --> super.onCreate(savedInstanceState)
        if(flag == 2 && methodName.equals("onCreate") && opcode == Opcodes.INVOKESPECIAL && name.equals("onCreate") && descriptor.equals("(Landroid/os/Bundle;)V") && !isInterface) {
            System.out.println("Initializing crash handler...");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/CrashHandler", "getInstance", "()Lrealtimecoverage/CrashHandler;", false);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, "getApplicationContext", "()Landroid/content/Context;", false);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "realtimecoverage/CrashHandler", "init", "(Landroid/content/Context;)V", false);

        }

        // recycler view
        if(className.startsWith(packageName) && (methodName.equals("onClick") || methodName.equals("onLongClick")) &&
                opcode == Opcodes.INVOKEVIRTUAL && owner.equals("android/view/View") && !isInterface){
            if(name.equals("getTag") && descriptor.equals("()Ljava/lang/Object;")) {
                System.out.println("Instrumenting：" + className + "/" + methodName + ", desc:" + desc);
                insertTime();
                mv.visitLdcInsn("viewId");
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/view/View", "getTag", "()Ljava/lang/Object;", false);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
            }
            else if(name.equals("getId") && descriptor.equals("()I")) {
                System.out.println("Instrumenting：" + className + "/" + methodName + ", desc:" + desc);
                insertTime();
                mv.visitLdcInsn("");
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/view/View", "getId", "()I", false);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;", false);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
            }
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(methodName);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visit", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        if (newStart != null) {
            start = newStart;
            newStart = null;
            super.visitLineNumber(line, start);
            this.lineNumber = line;
            return;
        }
        super.visitLineNumber(line, start);
    }

//    @Override
//    public void visitInsn(int opcode) {
////        if (flag && ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW)) {
////            mv.visitLdcInsn(className);
//////            mv.visitLdcInsn(methodName + "_" + lineNumber);
////            mv.visitLdcInsn(methodName);
////            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visitFinish", "(Ljava/lang/String;Ljava/lang/String;)V", false);
////        }
////        super.visitInsn(opcode);
//
//        if (flag && ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN))) {
//            mv.visitLdcInsn(className);
////            mv.visitLdcInsn(methodName + "_" + lineNumber);
//            mv.visitLdcInsn(methodName);
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visitFinish", "(Ljava/lang/String;Ljava/lang/String;)V", false);
//        }
//        else if (opcode == Opcodes.ATHROW) {
//            mv.visitLdcInsn(className);
////            mv.visitLdcInsn(methodName + "_" + lineNumber);
//            mv.visitLdcInsn(methodName);
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "visitFinish", "(Ljava/lang/String;Ljava/lang/String;)V", false);
//            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "realtimecoverage/MethodVisitor", "tearDown", "()V", false);
//        }
//        super.visitInsn(opcode);
//    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
