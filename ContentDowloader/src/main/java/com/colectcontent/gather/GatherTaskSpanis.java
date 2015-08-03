package com.colectcontent.gather;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/28/2015.
 */
public class GatherTaskSpanis extends GenateGatherTask{
    protected String getOutputPre(TitleInfo title) {
        return "ouput/gather/SP/"+gethtlmTitle(title)+"/";
    }
    protected String languageParam() {
        return "&language=sp";
    }
    protected String getOtionNAV() {
        return "<div class=\"chart nav-group-chart\">\n" +
                "                            <a target=\"grade-main\" class=\"nav-gather grade-cluster\"><img\n" +
                "                                    src=\"img/gather/gnav_grade.jpg\"></a>\n" +
                "                            <ul class=\"group-grade\">\n" +
                "                                <li>\n" +
                "                                    <a target=\"pre-school\" class=\"nav-gather grade-cluster bold-blue\">Pre escolar</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"kindergarten\" class=\"nav-gather grade-cluster bold-blue\">Kindergarten</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-1\" class=\"nav-gather grade-cluster bold-blue\">Prime curso</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-2\" class=\"nav-gather grade-cluster bold-blue\">Segundo curso</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-3\" class=\"nav-gather grade-cluster bold-blue\">Tercer curso</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-4\" class=\"nav-gather grade-cluster bold-blue\">Cuarto curso</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-5\" class=\"nav-gather grade-cluster bold-blue\">Quinto curso</a>\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"grade-6\" class=\"nav-gather grade-cluster bold-blue\">Sexto curso</a>\n" +
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
                "                                    <a target=\"cluster-pre-k\" class=\"nav-gather grade-cluster bold-blue\">Pre/K—K</a>\n" +
                "                                    Pre escolar (4 años) y kindergarten\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"cluster-primary\" class=\"nav-gather grade-cluster bold-blue\">Primary</a>\n" +
                "                                    Cursos 1, 2, 3\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"cluster-intermediate\" class=\"nav-gather grade-cluster bold-blue\">Intermediate</a>\n" +
                "                                    Cursos 4, 5, 6\n" +
                "                                </li>\n" +
                "\n" +
                "                                <li>\n" +
                "                                    <a target=\"cluster-junior-high\" class=\"nav-gather grade-cluster bold-blue\">Junior\n" +
                "                                        High</a>\n" +
                "                                    Cursos 7, 8\n" +
                "                                </li>\n" +
                "                            </ul>\n" +
                "                        </div>";
    }
    protected String getGradeNAVHead(TitleInfo title) {
        return " <span id=\"optionGrades\">\n" +
                "                    <a target=\"group-grade\" class=\"nav-group bold-orange\">Curso</a><br>\n" +
                "                    <a target=\"group-cluster\" class=\"nav-group bold-orange\">Grupo</a>\n" +
                "                </span>";
    }protected String replaceReplaceOption(String content) {
                Pattern linkPa = Pattern.compile("<a\\s+href=\"([^\"]+)\"[^>]*>Opci&oacute;n 1");
                Matcher matcher = linkPa.matcher(content);
                if(matcher.find()){
                content = content.replaceAll(Pattern.quote(matcher.group(1)), "option1.html");
                linkPa = Pattern.compile("<a\\s+href=\"([^\"]+)\"[^>]*>Opci&oacute;n 2");
                matcher = linkPa.matcher(content);
                matcher.find();
                content = content.replaceAll(Pattern.quote(matcher.group(1)), "option2.html");
                }
                return content;
        }
    public static void main(String[] args) {
            GatherTaskSpanis task = new GatherTaskSpanis();
        task.dowork();
    }
    protected String getNavBar() {
        return " <ul>\n" +
                "                            <li>\n" +
                "                                <a target=\"guild-prep-chart\" class=\"nav-gather bold-blue\">Celebrando la Epifanía</a> <br><br>\n" +
                "                            </li>\n" +
                "                            <li>\n" +
                "                                <a target=\"before-gather\" class=\"nav-gather bold-blue\">Antes de la reunión</a> <br><br>\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                <a target=\"we-gather\" class=\"nav-gather bold-blue\">Nos reunimos</a> <br><br>\n" +
                "                            </li>\n" +
                "\n" +
                "                            <li>\n" +
                "                                <a target=\"after-gather\" class=\"nav-gather bold-blue\">Después de la reunión</a>\n" +
                "                            </li>\n" +
                "                        </ul>";
    }
}
