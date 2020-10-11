# MinecraftAssetsDownloader
A simple Java library to download or get Minecraft assets of a specific version

## How to use ?
Import [this jar file](https://github.com/Shawiizz/MinecraftAssetsDownloader/blob/master/MCAssetsDownloader.jar?raw=true) into your project.

### Getting assets only
-> `MCAssetsDownloader.getAssets("version", LOG_DISPLAY);`
- Replace version by your choosed version. To get assets of the latest version, replace version by release or snapshot.
- If you don't want to display logs, replace LOG_DISPLAY by LOG_HIDE

> This method return a List of String

### Download assets
-> `MCAssetsDownloader.downloadAssets("version", "path", LOG_DISPLAY);`
- Replace version by your choosed version. To download assets of the latest version, replace "version" by "release" or "snapshot".
- Replace path by your own folder path.
- If you don't want to display logs, replace LOG_DISPLAY by LOG_HIDE

## Data available

You can get files to download and downloaded files. Theses things are store in 2 int variables : `filesToDownload` and `downloadedFiles`
