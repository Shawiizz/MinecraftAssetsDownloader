package fr.shawiizz;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class MCAssetsDownloader {
    public static final boolean LOG_HIDE = false;
    public static final boolean LOG_DISPLAY = true;
    private static boolean log = true;
    public static int downloadedFiles = 0;
    public static int filesToDownload = 0;
    public static String currentFile = "";

    public static void downloadAssets(String version, String path, boolean log) throws Exception {
        MCAssetsDownloader.log = log;
        List<String> index = getAssets(version, log);
        for (String asset : index)
            if(!new File(path + "/assets/objects/" + asset.substring(0, 2) + "/" + asset).exists())
                filesToDownload++;

        log("There is "+filesToDownload+" assets to download.");

        for (String asset : index) {
            String assetPath = asset.substring(0, 2) + "/" + asset;
            String filePath = path + "/assets/objects/" + assetPath;
            File anAsset = new File(filePath);
            anAsset.getParentFile().mkdirs();
            try {
                log("Downloading "+assetPath);
                currentFile = filePath;
                FileChannel fileChannel = FileChannel.open(anAsset.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                fileChannel.transferFrom(Channels.newChannel(new URL("http://resources.download.minecraft.net/"+assetPath).openConnection().getInputStream()), 0L, Long.MAX_VALUE);
                log("Downloaded "+assetPath);
                downloadedFiles++;
            } catch (IOException e) {
                log("[ERROR] Unable to download asset "+assetPath);
            }
        }
    }

    public static List<String> getAssets(String version, boolean log) throws Exception{
        MCAssetsDownloader.log = log;
        List<String> assets = new ArrayList<>();
        for(String ver : getPageContent("https://launchermeta.mojang.com/mc/game/version_manifest.json").split("}")) {
            if(version.equals("snapshot")) version = ver.split("\"snapshot\": \"")[1].split("\"")[0];
            if(version.equals("release")) version = ver.split("\"snapshot\": \"")[1].split("\"")[0];

            if(!ver.contains("latest") && ver.contains("\"" + version + "\"")) {
                String jsonPage = getPageContent(ver.split("\"url\": \"")[1].split("\"")[0]);
                for(String j : getPageContent(jsonPage.split(".json")[0].split("\"url\": \"")[1] + ".json").replace("{\"objects\": {", "").split("},"))
                    assets.add(j.split("hash\": \"")[1].split("\"")[0]);
            }
        }
        return assets;
    }

    private static String getPageContent(String u) throws Exception {
        URLConnection c = new URL(u).openConnection();
        c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        c.connect();
        return new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8)).readLine();
    }

    private static void log(String t) {
        if(log)
            System.out.println("[MCAssetsDownloader] - "+t);
    }
}
