package com.colectcontent.gather;

import com.colectcontent.MyTask;
import com.colectcontent.SimpleThread;
import com.colectcontent.com.colectcontent.imple.FindStrategyTemplate2;
import com.until.Untils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/25/2015.
 */
public class GenateGatherTask {
    String mainPageLink = "http://www.sadlierreligion.com/webelieve/gather.cfm?page=epiphany";
    //private String imgContent;
    //String[] titles = new String[]{"epiphany","katrina","eucharist","mary","pentecost","easter","christmas"};

    //String[] titles = new String[]{"eucharist"};
    //private String curentTitle;
    //private boolean isOption =false;
    FindStrategy strategy1 = new FindStrategyTemplate2();

    public void dowork(){
        List<TitleInfo> titles = new ArrayList<>();
        titles.add(new TitleInfo("garden",false,languageParam(),getOtionNAV()));
        //titles.add(new TitleInfo("epiphany",false,languageParam(),getOtionNAV()));
        //titles.add(new TitleInfo("katrina",false,languageParam(),getOtionNAV()));
        //titles.add(new TitleInfo("eucharist",true,languageParam(),getOtionNAV()));
        //titles.add(new TitleInfo("mary",true,languageParam(),getOtionNAV()));
        //titles.add(new TitleInfo("pentecost",true,languageParam(),getOtionNAV()));
        //titles.add(new TitleInfo("easter",true,languageParam(),getOtionNAV()));
        //titles.add(new TitleInfo("christmas",true,languageParam(),getOtionNAV()));


        for(final TitleInfo item:titles){
            SimpleThread doThread = new SimpleThread(new MyTask() {
                @Override
                public Object doInBackGround() {
                    //curentTitle = item.getName();
                    //isOption = item.isoption();
                    doworrk(item);
                    return null;
                }

                @Override
                public void onSuccess(Object result) {

                }
            });
            doThread.startThread();

        }
    }

    protected String getOtionNAV() {
        return "<div class=\"chart nav-group-chart\">\n" +
                "                            <a target=\"grade-main\" class=\"nav-gather grade-cluster\"><img\n" +
                "                                    src=\"img/gather/gnav_grade.jpg\"></a>\n" +
                "                            <ul class=\"group-grade\">\n" +
                "                                <li>\n" +
                "                                    <a target=\"pre-school\" class=\"nav-gather grade-cluster bold-blue\">Pre-school</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"kindergarten\" class=\"nav-gather grade-cluster bold-blue\">Kindergarten</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-1\" class=\"nav-gather grade-cluster bold-blue\">Grade 1</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-2\" class=\"nav-gather grade-cluster bold-blue\">Grade 2</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-3\" class=\"nav-gather grade-cluster bold-blue\">Grade 3</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-4\" class=\"nav-gather grade-cluster bold-blue\">Grade 4</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-5\" class=\"nav-gather grade-cluster bold-blue\">Grade 5</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-6\" class=\"nav-gather grade-cluster bold-blue\">Grade 6</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-78\" class=\"nav-gather grade-cluster bold-blue\">Grade 7 &amp; 8</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                            </ul>\n" +
                "\n" +
                "                            <ul class=\"group-cluster\">\n" +
                "                                <li>\n" +
                "                                    <a target=\"cluster-pre-k\" class=\"nav-gather grade-cluster bold-blue\">Pre/K�K</a>\n" +
                "                                    Pre-school (age 4) and Kindergarten\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"cluster-primary\" class=\"nav-gather grade-cluster bold-blue\">Primary</a>\n" +
                "                                    Grades 1, 2, 3\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"cluster-intermediate\" class=\"nav-gather grade-cluster bold-blue\">Intermediate</a>\n" +
                "                                    Grades 4, 5, 6\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"cluster-junior-high\" class=\"nav-gather grade-cluster bold-blue\">Junior\n" +
                "                                        High</a>\n" +
                "                                    Grades 7, 8\n" +
                "                                </li>\n" +
                "                            </ul>\n" +
                "                        </div>";
    }

    public void doworrk(TitleInfo title){

        //String pageContent = getContentFromFullCode(mainPageLink);

        coppyAssetFiles(title);
        String outPutdir= getOutputPre(title);

        String preCharContent = midPreChar(title);
        String beforeGather = midbeforeGathe(title);
        String anounContent = midAnounContent(title);
        String weGather = midweGather(title);
        String aftherGather = midaftherGather(title);
        String teamBackGround = midTemBackGround(title);



        StringWriter stringWriter = null;
        try{
            InputStream inputStream = new FileInputStream(new File("template\\gather.html"));
            stringWriter = new StringWriter();
            IOUtils.copy(inputStream, stringWriter, "UTF-8");
        } catch (Exception e) {

        }
        String fileTemplate = stringWriter.toString();

        fileTemplate = fileTemplate.replace("#announcement-content#",anounContent);
        //fileTemplate = fileTemplate.replace("#prep-chart-invitation-letter#",mainContent);
        fileTemplate = fileTemplate.replace("#before-gather-letter-content#",beforeGather);
        fileTemplate = fileTemplate.replace("#team-background-content#",teamBackGround);
        fileTemplate = fileTemplate.replace("#we-gather-content#",weGather);
        fileTemplate = fileTemplate.replace("#after-gather-content#", aftherGather);
        fileTemplate = fileTemplate.replace("#ChartCode#", getNavBar());


        String teplateOption1 = fileTemplate;


        teplateOption1 = fileTemplate.replace("#prep-chart-content#",preCharContent);
        fileTemplate = fileTemplate.replace("#imgHeader#", title.getImgContent());
        teplateOption1 = teplateOption1.replace("#imgHeader#", title.getImgContent());

        String GradeNAV ="";
        String NAVHead ="";
        if(title.isoption) {
            GradeNAV = getGradeNAV(title);
            NAVHead = getGradeNAVHead(title);
            String  preCharContent2 = midPreCharOp2(title);
            String optionChosePageContent = midChosePageContent(title);
            stringWriter = null;
            try{
                InputStream inputStream = new FileInputStream(new File("template\\gather_chose_option.html"));
                stringWriter = new StringWriter();
                IOUtils.copy(inputStream, stringWriter, "UTF-8");
            } catch (Exception e) {

            }
            String choseopTemplate = stringWriter.toString();
            choseopTemplate = choseopTemplate.replace("#content#",optionChosePageContent);

            fileTemplate = fileTemplate.replace("#prep-chart-content#", preCharContent2);
            String gradeBock = "";
            for (GatherPageModel itemg : title.getGradePages()) {
                gradeBock += midContent(itemg,title) + "\n";
            }
            String ClusterBlock = "";
            for (GatherPageModel itemg : title.getClusterPages()) {
                ClusterBlock += midContent(itemg,title) + "\n";
            }
            fileTemplate = fileTemplate.replace("#groupGradeContent#", gradeBock);
            fileTemplate = fileTemplate.replace("#groupClusterContent#", ClusterBlock);

            fileTemplate = fileTemplate.replace("#OptionNVBCode#", GradeNAV);
            fileTemplate = fileTemplate.replace("#NAVHeader#", NAVHead);
            try {
                FileUtils.writeStringToFile(new File(outPutdir + "\\" + "option2.html"), fileTemplate, "UTF-8");
                FileUtils.writeStringToFile(new File(outPutdir + "\\" + title.getName()+".html"), choseopTemplate, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        teplateOption1 = teplateOption1.replace("#groupGradeContent#","");
        teplateOption1 = teplateOption1.replace("#groupClusterContent#","");
        teplateOption1 = teplateOption1.replace("#OptionNVBCode#","");
        teplateOption1 = teplateOption1.replace("#NAVHeader#","");


        File outDir = new File(outPutdir);
        if(!outDir.exists()){
            outDir.mkdirs();
        }
        //String workingLink = Untils.getWorkingLinkFromLink(item.getResourceDataLink());
        //detectAndDowImg(item.getMainContent(),outPutdir,workingLink);
        //String refacContent = item.getMainContent().replaceAll("\"\\.\\./","\"");
        //refacContent = refacContent.replaceAll("\'\\.\\./", "'");

        try {
            FileUtils.writeStringToFile(new File(outPutdir + "\\" + "option1.html"), teplateOption1, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String midChosePageContent(TitleInfo title) {
        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+languageParam();
        StringBuilder stringbd = new StringBuilder();
        String content =  strategy1.mindNomalcontentFromFullCode(link, getOutputPre(title));

        content = replaceReplaceOption(content);



        //imgContent = stringbd.toString();
        return content ;
    }

    protected String replaceReplaceOption(String content) {
        Pattern linkPa = Pattern.compile("<a\\s+href=\"([^\"]+)\"[^>]*>Option 1");
        Matcher matcher = linkPa.matcher(content);
        if(matcher.find()){
        content = content.replaceAll(Pattern.quote(matcher.group(1)), "option1.html");
        linkPa = Pattern.compile("<a\\s+href=\"([^\"]+)\"[^>]*>Option 2");
        matcher = linkPa.matcher(content);
        matcher.find();
        content = content.replaceAll(Pattern.quote(matcher.group(1)),"option2.html");}
        return content;
    }

    private String midPreCharOp2(TitleInfo title) {
        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+"&sp=option2"+languageParam();
        StringBuilder stringbd = new StringBuilder();
        String content =  strategy1.midPreChar(link, getOutputPre(title), stringbd, getNavBar());
        title.setImgContent(stringbd.toString());
        return content ;
    }

    protected String getGradeNAVHead(TitleInfo title) {
        return " <span id=\"optionGrades\">\n" +
                "                    <a target=\"group-grade\" class=\"nav-group bold-orange\">Grade</a><br>\n" +
                "                    <a target=\"group-cluster\" class=\"nav-group bold-orange\">Cluster</a>\n" +
                "                </span>";
    }

    private String getGradeNAV(TitleInfo title) {
        return title.getNVBcode();
    }

    private String midContent(GatherPageModel item,TitleInfo title) {
        String blockTemplate = "<div class=\"gather-block\" id=\"#id#\">#content#</div>";
        String content = strategy1.mindNomalcontentFromFullCode(item.contentLink.replace("{title}",title.getName()), getOutputPre(title));
        System.out.println("Mid grade content " + title.getName()+"---"+item.getId());
        blockTemplate = blockTemplate.replace("#id#", item.getId());
        blockTemplate = blockTemplate.replace("#content#",content);
        return blockTemplate;
    }

    private void coppyAssetFiles(TitleInfo title) {
        String inputAssets = getInputAssetPath();
        Untils.coppyDir(inputAssets,getOutputPre(title));

    }

    private String getInputAssetPath() {
        return "template/assets/gather";
    }

    protected String gethtlmTitle(TitleInfo title) {

        return title.getName();
    }

    protected String getOutputPre(TitleInfo title) {
        return "ouput/gather/En/"+gethtlmTitle(title)+"/";
    }

    private String midAnounContent(TitleInfo title) {
        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+"&sp=option1&tp=bulletin"+languageParam();
        return strategy1.midAnounContent(link, getOutputPre(title));
    }

    private String midTemBackGround(TitleInfo title) {
        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+"&sp=option1&tp=catechist"+languageParam();

        return strategy1.midTemBackGround(link, getOutputPre(title));
    }

    protected String languageParam() {
        return "";
    }

    private String midaftherGather(TitleInfo title) {
        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+"&sp=option1&tp=after"+languageParam();

        return strategy1.midaftherGather(link, getOutputPre(title));
    }

    private String midweGather(TitleInfo title) {

        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+"&sp=option1&tp=gather&section={section}"+languageParam();

        return strategy1.midweGather(link, getOutputPre(title));
    }

    private String midbeforeGathe(TitleInfo title) {
        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+"&sp=option1&tp=before"+languageParam();

        return strategy1.midbeforeGathe(link, getOutputPre(title));
    }

    private String midPreChar(TitleInfo title) {
        String link ="http://www.sadlierreligion.com/webelieve/gather.cfm?page="+gethtlmTitle(title)+"&sp=option1"+languageParam();
        StringBuilder stringbd = new StringBuilder();
        String content =  strategy1.midPreChar(link, getOutputPre(title), stringbd, getNavBar());
        title.setImgContent(stringbd.toString());
        return content ;
    }

    private void detectAndDowImg(String content, final String outputPre, final String workingLinkFromLink) {
        final String contentHolder = content;
        SimpleThread task = new SimpleThread(new MyTask() {
            @Override
            public Object doInBackGround() {
                Untils.detectAndDowImg(contentHolder,outputPre,workingLinkFromLink);
                return  null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
        task.startThread();
        content = content.replaceAll("\"\\.\\./", "\"");
        content = content.replaceAll("\'\\.\\./", "'");
    }

    protected String getNavBar() {
        return  "                            <li>\n" +
                "                                <a target=\"guild-prep-chart\" class=\"nav-gather bold-blue\">Prep Charts</a> <br><br>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                <a target=\"before-gather\" class=\"nav-gather bold-blue\">Before We Gather</a> <br><br>\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                <a target=\"we-gather\" class=\"nav-gather bold-blue\">We Gather</a> <br><br>\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                <a target=\"after-gather\" class=\"nav-gather bold-blue\">After We Gather</a>\n" +
                "                            </li>\n";
    }

    private String getContentFromFullCode(String link) {
        Document document = null;
        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element ele =  document.getElementById("content");
        if(ele==null){
            ele =  document.getElementsByAttributeValue("valign","TOP").get(0).parent().parent();
        }

        //content = content.replaceAll("(?s)<script language=\"JavaScript\">(.+?)</script>","");
        if(ele.getElementsByAttributeValue("class","chart")!=null){
            ele.getElementsByAttributeValue("class","chart").remove();
        }
        String content = ele.outerHtml();
        return content;
    }

    public static void main(String[] args) {
        GenateGatherTask task = new GenateGatherTask();
        task.dowork();
    }
}
