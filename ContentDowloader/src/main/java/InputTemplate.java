/**
 * Created by vincent on 7/16/2015.
 */
public class InputTemplate {
    String category;
    String htmlLink;
    String regexFindImage;

    public InputTemplate(String category, String htmlLink, String regexFindImage) {
        this.category = category;
        this.htmlLink = htmlLink;
        this.regexFindImage = regexFindImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public String getRegexFindImage() {
        return regexFindImage;
    }

    public void setRegexFindImage(String regexFindImage) {
        this.regexFindImage = regexFindImage;
    }
}
