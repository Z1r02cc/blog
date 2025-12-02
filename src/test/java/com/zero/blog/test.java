package com.zero.blog;

import cn.hutool.core.date.DateUtil;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

public class test {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println((ResourceUtils.getURL("classpath:").getPath()).split("target")[0]);
    }
}
