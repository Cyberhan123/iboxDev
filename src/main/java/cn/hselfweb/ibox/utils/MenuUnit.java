package cn.hselfweb.ibox.utils;

import cn.hselfweb.ibox.bean.Menu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuUnit {
    public List<Menu> meiShiJieHotDownLoad(String seach) {
        List<Menu> menus = null;
        String url = "https://so.meishi.cc/?q=" + seach;
        try {

            Document document = Jsoup.connect(url).get();
            menus = meiShiJieProcess(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menus;
    }

    private static ArrayList<Menu> meiShiJieProcess(Document document) {
        ArrayList<Menu> menus = new ArrayList<>();
        Elements getMenuLink = document.select(".info a");
        List<String> urls = new ArrayList<>();
        for (Element element : getMenuLink) {
            String url = element.attr("href");
            urls.add(url);
            //  System.out.println(url);
        }

        List<String> steps = new ArrayList<>();
        for (String url : urls) {
            Menu menu = new Menu();
            try {
                Document doc = Jsoup.connect(url).get();
                Elements title = doc.select(".measure h2");
                String[] titleSelect = title.text().split("\\s");
                menu.setTitle(titleSelect[0]);
                System.out.println(titleSelect[0]);
                Elements imgs = doc.select(".measure p img");
                for (Element element : imgs) {
                    String imgUrl = element.attr("src");
                    steps.add(imgUrl);
                  //  System.out.println(imgUrl);
                }
                Elements descript = doc.select(".measure p>em");
                System.out.print(descript.text());
                String[] descripts = descript.text().split("\\s");
                steps.addAll(Arrays.asList(descripts));
                menu.setSteps(steps);
                menu.setMenuUrl(url);
                menus.add(menu);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return menus;
        }
        return menus;
    }

}
