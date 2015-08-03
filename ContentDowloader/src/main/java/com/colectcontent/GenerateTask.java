package com.colectcontent;

import com.until.Untils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 7/17/2015.
 */
public abstract class GenerateTask {

    private List<ResourceInfo> resourceInfoList;
    private String workingOnCategory;
    protected String[] Grade= new String[]{"","","",""};
    protected String workingOnGrade="";

    public void doWork(){
        setUpWorkingGrade();
        for(String gradeitem:Grade){
            workingOnGrade = gradeitem;
            readResourceInfo(resourceInfoList);
            midResourceLinkData(resourceInfoList, workingOnGrade);
            midResourceContent(resourceInfoList);
            resourceInfoList.clear();
        }
    }

    protected abstract void setUpWorkingGrade();


    public void midResourceContent(List<ResourceInfo> resourceInfoList){
        for(final ResourceInfo item :resourceInfoList){
            SimpleThread work = new SimpleThread(new MyTask() {
                @Override
                public Object doInBackGround() {
                    if(item.getResourceDataLink()!=null) {
                        item.setMainContent(genContentFromLink(item.getResourceDataLink()));
                        item.setMainContent(midResourceGetFullContent(item.getMainContent()));
                        genarateFiles(item);
                    }else{
                        System.out.println("Error Title: "+item.getTitle()+" Grade:"+item.getGrade() +" coud not mapping!");
                    }
                    return null;
                }

                @Override
                public void onSuccess(Object result) {

                }
            });
            work.startThread();

        }
    }

    protected abstract void genarateFiles(ResourceInfo item);

    /**
     * Th�m teamplate cho content
     * @param mainContent
     * @return
     */
    protected abstract String midResourceGetFullContent(String mainContent);

    /**
     * L?y content t? link d?n ??n b�i
     * @param resourceDataLink
     * @return
     */
    protected abstract String genContentFromLink(String resourceDataLink);

    /**
     * t�m c�ch l?y t?t c? c�c ???ng link d?n ??n trang content
     * vd http://www.sadlierreligion.com/webelieve/prayers.cfm?sp=teacher&section=resource&grade=1&view=&id=22
     *
     * @param resourceInfoList
     */
    protected abstract void midResourceLinkData(List<ResourceInfo> resourceInfoList,String grade);

    private void readResourceInfo(List<ResourceInfo> outList) {
        resourceInfoList = new ArrayList<>();
        readResourceCode(workingOnGrade, WorkingOnCategory(), resourceInfoList);

    }

    /**
     * category ?ang s? d?ng
     * @return
     */
    protected abstract String WorkingOnCategory();

    private void readResourceCode(String grade, String tcategory, List<ResourceInfo> resourceInfoList) {
        try {
            //String file="input\\RWB_ENG\\G{grade}.xlsx";
            String file= getInputTemplateWithGrade();

            file = file.replaceAll("\\{grade\\}",grade);
            //POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            String inputPath =  System.getProperty("user.dir")+"\\"+file;
            System.out.println(inputPath);
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(inputPath));
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            XSSFCell cell;

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

            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row.getCell(3).getStringCellValue().equals(tcategory)){
                    String category =  row.getCell(3).getStringCellValue();
                    String resource = row.getCell(0).getStringCellValue();
                    String title = row.getCell(2).getStringCellValue();
                    System.out.println(resource + "--------" + title);
                    ResourceInfo res = new ResourceInfo(resource,title,category);
                    res.setGrade(grade);
                    resourceInfoList.add(res);
                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

    protected abstract String getInputTemplateWithGrade();


    public void detectAndDowImg(String mainContent, String outPutdir,String workingLink) {
        Pattern imgPattern = Pattern.compile("<img src=\"([^\"]+)\"");
        Pattern pdfPattern = Pattern.compile("<a\\s+href=\"((?!http)([^\"]+).pdf)\"");
        List<String> Links = new ArrayList<>();
        Matcher matcher = imgPattern.matcher(mainContent);
        while(matcher.find()){
            if (!Links.contains(matcher.group(1))){
                Links.add(matcher.group(1));
            }
        }
        matcher = pdfPattern.matcher(mainContent);
        while(matcher.find()){
            if (!Links.contains(matcher.group(1))){
                Links.add(matcher.group(1));
            }
        }
        for(String link:Links){
            String fullLink = Untils.getFullDowloadLink(workingLink, link);
            String dirFomLink = Untils.getDirfromLink(link);
            File imgfolder = new File(outPutdir+"\\"+dirFomLink);
            if(!imgfolder.exists()){
                imgfolder.mkdirs();
            }
            String fileName = Untils.getFileNameFromLink(link);
            Untils.dowloadFile(fullLink, outPutdir + "\\" + dirFomLink + "\\" + fileName);
        }
    }

}
