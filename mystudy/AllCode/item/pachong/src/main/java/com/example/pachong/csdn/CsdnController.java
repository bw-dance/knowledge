package com.example.pachong.csdn;

import com.example.pachong.html2markdown.HTML2Md;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.markdownj.MarkdownProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Classname CsdnController
 * @Description TODO
 * @Date 2022/2/17 19:42
 * @Created by zhq
 */

@RestController
@RequestMapping("/api/csdn")
public class CsdnController {

    @GetMapping
    public Object getArticleByUrl(String url) throws IOException {
//        WebClient webClient = WebClient.create();
//        Mono<String> mono = webClient.get().uri("https://blog.csdn.net/Fine____/article/details/120576478").retrieve().bodyToMono(String.class);
//
//        System.out.println(mono.block());

        Document doc = Jsoup.connect("https://blog.csdn.net/Fine____/article/details/121232329").get();
        //文章标题
        String title = doc.title() + ".html";
        System.out.println(title);
        Elements article = doc.getElementsByTag("article");
        //文章内容
        String content = article.html();
        //写入桌面
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:/Users/DELL/Desktop/" + title));
//        bufferedWriter.write(content);
//        MarkdownProcessor markdownProcessor = new MarkdownProcessor();
//        String markdown = markdownProcessor.markdown(content);
//        System.out.println(markdown);
        System.out.println(this.getClass().getClassLoader()
                .getResource(title)//获取资源（默认从类路径（当前模块的src）下加载资源）
                .getPath());
        HTML2Md.htmlToHexoMd(this.getClass().getClassLoader()
                .getResource( title)//获取资源（默认从类路径（当前模块的src）下加载资源）
                .getPath(), "file/", "");//获取绝对路径（该方法跨平台）,"C:/Users/DELL/Desktop/","");


        return null;
    }


}
