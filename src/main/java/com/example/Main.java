package com.example;

import fish.payara.micro.PayaraMicro;
import fish.payara.micro.PayaraMicroRuntime;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File warFile = new File("lib/rest.war");
        System.out.println(warFile.getAbsolutePath());
        System.out.println(warFile.exists());
        
        PayaraMicro payaraMicro = PayaraMicro.getInstance();
        payaraMicro.addDeploymentFile(warFile);
//        payaraMicro.setHttpPort(8000); // ポート番号を8080から変えたい場合はコメントを外す
        PayaraMicroRuntime runtime = payaraMicro.bootStrap();
    }
}
