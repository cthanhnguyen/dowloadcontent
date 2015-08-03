package com.tool.dowloadresource;

import com.colectcontent.MyTask;
import com.colectcontent.ResourceInfo;
import com.colectcontent.SimpleThread;
import com.tool.ProductMeta;
import com.until.Untils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/24/2015.
 */
public class DowloadResourceByCategory {
    static final String dowloadprefix = "http://192.168.2.12:8090/cms";
    List<ResourceInfo> resourceInfoList = new ArrayList<>();
    public void doWork(){
       readResourceByCategory();

       for(final ResourceInfo item:resourceInfoList){
           SimpleThread task = new SimpleThread(new MyTask() {
               @Override
               public Object doInBackGround() {
                   String assetFilePath = item.getAssetPath();
                   String dowLoadUri = toRealAssetUrl(assetFilePath);
                   String ouputTemplate = getOutputTemplate(item);

                   List<String> listFiles = getFilesFromWeb(dowLoadUri);
                   dowLoadFiles(listFiles,ouputTemplate);
                   System.out.println("Dowloaded resource "+item.getResourceCode()+"!");
                   return null;
               }

               @Override
               public void onSuccess(Object result) {

               }
           });
           task.startThread();
       }
    }

    private void dowLoadFiles(List<String> listFiles, String ouputTemplate) {
        for(String dirLink:listFiles){
            //System.out.println("link : " + dirLink);
            String ext = Untils.getFileExtFromLink(dirLink);
            if(dowLoadFile(ext)){
                Untils.dowloadFile(dowloadprefix + dirLink, ouputTemplate + dirLink);
            }
        }
    }

    private boolean dowLoadFile(String ext) {
        String dowloadext = getExtendSionList();
        if(!dowloadext.isEmpty()){
            return dowloadext.contains(ext);
        }
        return true;
    }

    protected String getExtendSionList() {
        return "";
    }

    private String getOutputTemplate(ResourceInfo item) {
        return "output/dowload/"+item.getCategory();

    }

    private void readResourceByCategory() {
        ProductMeta[] metas = getProdoctCodeList();
        for(ProductMeta item :metas){
           readResourceCode(item.getGrateTOC(),getWorkingOnCategory(),resourceInfoList);
        }
    }

    protected String getWorkingOnCategory() {
        return "Lives of The Saints";
    }
    protected String getInputTemplateWithGrade() {

        return "input/WB/WB_EN_{grade}.xls";
    }

    protected ProductMeta[] getProdoctCodeList() {
        return new ProductMeta[]{new ProductMeta("WB_EN_K","752","K"),
                new ProductMeta("WB_EN_1","751","1"),
                new ProductMeta("WB_EN_2","750","2"),
                new ProductMeta("WB_EN_3","749","3"),
                new ProductMeta("WB_EN_4","748","4"),
                new ProductMeta("WB_EN_5","747","5"),
                new ProductMeta("WB_EN_6","746","6")
        };
    }

    private List<String> getFilesFromWeb(String dowLoadUri) {
        String listFileLink = "http://192.168.2.12:8090/cms/product/listAssetsWithUrl.html";
        List<NameValuePair> formData = new ArrayList<>();
        formData.add(new BasicNameValuePair("path",dowLoadUri));
        String htmlCode = Untils.postHtmlFromURL(listFileLink,formData);
        Pattern filePatten = Pattern.compile("<td>(" + dowLoadUri + "[^<]+)</td>");
        Matcher matcher = filePatten.matcher(htmlCode);
        List<String> result = new ArrayList<>();
        while (matcher.find()){
            result.add(matcher.group(1));
        }
        return result;
    }

    private String toRealAssetUrl(String assetFilePath) {
        assetFilePath = assetFilePath.replaceAll("assets/","/content/");
        return assetFilePath.replaceAll("/[^/]+$", "/");
    }
    private void readResourceCode(String grade, String tcategory, List<ResourceInfo> resourceInfoList) {
        try {
            //String file="input\\RWB_ENG\\G{grade}.xlsx";
            String file= getInputTemplateWithGrade();

            file = file.replaceAll("\\{grade\\}",grade);
            //POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            String inputPath =  System.getProperty("user.dir")+"\\"+file;
            System.out.println(inputPath);
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(inputPath));
            HSSFSheet sheet = wb.getSheetAt(2);
            HSSFRow row;

            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }
            String gamePre = getGamePreTemplate();
            gamePre = gamePre.replace("{grade}",grade);
            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row.getCell(3).getStringCellValue().equals(tcategory)){
                    String category =  row.getCell(3).getStringCellValue();
                    String resource = row.getCell(0).getStringCellValue();
                    String title = row.getCell(2).getStringCellValue();
                    String assestPath = row.getCell(10).getStringCellValue();
                    if (category.equals("Games")){
                        assestPath = assestPath.replace("assets/games/",gamePre);
                    }
                    System.out.println(resource + "--------" + title);
                    ResourceInfo res = new ResourceInfo(resource,title,category);
                    res.setGrade(grade);
                    res.setAssetPath(assestPath);
                    resourceInfoList.add(res);

                }
            }

        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

    protected String getGamePreTemplate() {
        return "asset/WB_EN_{grade}/";
    }


    public static void main(String[] args) {
        Untils.innit();
        DowloadResourceByCategory task = new DowloadResourceByCategory();
        task.doWork();
    }
}
