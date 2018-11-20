package cn.hselfweb.ibox.bean;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    String MenuUrl;
    String title;
    List<String> steps;
}
