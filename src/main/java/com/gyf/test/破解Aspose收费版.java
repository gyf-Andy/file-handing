package com.gyf.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.Test;

/**
 * @author 郭云飞
 * @date 2021/12/20-14:23
 * @Description TODO
 */
public class 破解Aspose收费版 {


    /**
     * 破解aspose-words
     * @throws Exception
     */
    @Test
    public void crackAsposeWords() throws Exception {
        ClassPool.getDefault().insertClassPath("C:\\maven\\maven-repository\\com\\aspose\\aspose-words\\20.12\\aspose-words-20.12-jdk17.jar");
        CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.words.zzZDZ");
        CtMethod zzZ4u = zzZJJClass.getDeclaredMethod("zzZ4n");
        CtMethod zzZ4t = zzZJJClass.getDeclaredMethod("zzZ4m");
        zzZ4u.setBody("{return 1;}");
        zzZ4t.setBody("{return 1;}");
        zzZJJClass.writeFile("C:\\maven\\maven-repository\\com\\aspose\\aspose-words\\20.12\\");
    }

    /**
     * 破解aspose-pdf
     * @throws Exception
     */
    @Test
    public void crackAsposePdf() throws Exception {
        ClassPool.getDefault().insertClassPath("C:\\maven\\maven-repository\\com\\aspose\\aspose-pdf\\20.8\\aspose-pdf-20.8.jar");
        CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.pdf.ADocument");
        CtMethod[] zzv = zzZJJClass.getDeclaredMethods();
        for (CtMethod m:zzv){
            CtClass[] ps=m.getParameterTypes();
            if (ps.length==2&&m.getName().equals("lI")&&ps[0].getName().equals("com.aspose.pdf.ADocument")){
                System.out.println(ps[0].getName());
                System.out.println(ps[1].getName());
            }
            if (m.getName().equals("lI")&&ps.length==2&&ps[0].getName().equals("com.aspose.pdf.ADocument")&&ps[1].getName().equals("int")){
                m.setBody("{return false;}");
            }
            if(m.getName().equals("lt")&&ps.length==0){
                m.setBody("{return true;}");
            }
        }
        zzZJJClass.writeFile("C:\\maven\\maven-repository\\com\\aspose\\aspose-pdf\\20.8\\");
    }


}
