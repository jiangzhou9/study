package com.yq.imageviewer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yq.imageviewer.Const;
import com.yq.imageviewer.bean.CoverItem;
import com.yq.imageviewer.event.LoadFinishEvent;
import com.yq.imageviewer.utils.des.DesUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.yq.imageviewer.Const.FILE_END;
import static com.yq.imageviewer.utils.ImageDownloadUtils.DOWNLOAD_FIRST_IMAGE_NAME;

/**
 * Created by yangqiang on 08/02/2018.
 */

public class FileUtil {

    public static int convert(String path) {
        File topDir = new File(path);
        if (!topDir.exists() || !topDir.isDirectory()) {
            return 0;
        }

        File[] pages = topDir.listFiles();
        int count = 0;
        for (File page : pages) {
            boolean needConvert = false;
            File newDir = null;
            try {
                String dirNameDecrypted = DesUtil.decrypt(page.getName());
                System.out.println(dirNameDecrypted);
            } catch (Exception e) {
                newDir = renameDirToEncryped(page);
                needConvert = true;
                count++;
            }

            if (needConvert) {
                renameFileEnd(newDir);
            }
        }
        return count;
    }

    public static File renameDirToEncryped(String dirName) {
        return renameDirToEncryped(new File(dirName));
    }
    //只把文件夹转为加密文件名
    public static File renameDirToEncryped(File oldFile) {
        return renameDirToEncryped(oldFile, null);
    }

    /**
     * 旧文件名改为新文件名
     * @param newFileName 新文件名（未加密）
     */
    public static File renameDirToEncryped(File oldFile, String newFileName) {
        if (oldFile == null) {
            return null;
        }
        if (TextUtils.isEmpty(newFileName)) {
            newFileName = oldFile.getName();
        }
        File newFile = new File(oldFile.getParent(), DesUtil.encrypt(newFileName));
        oldFile.renameTo(newFile);

        return newFile;
    }

    //文件夹下文件改为固定后缀（文件夹已加密）
    public static void renameFileEnd(File dir) {
        if (dir == null) {
            return;
        }
        File[] children = dir.listFiles();
        if (children == null) {
            return;
        }
        for (File oldFile : children) {
            String oriFileName = oldFile.getName();
            String newFileName;
            if (oriFileName.contains(".")) {
                newFileName = oldFile.getName().substring(0, oriFileName.lastIndexOf(".")) + FILE_END;
            } else {
                newFileName = oriFileName + FILE_END;
            }

            oldFile.renameTo(new File(oldFile.getParent(), newFileName));
        }
    }

    public static void getCovers(String path, final GetCoverListener listener) {
        Observable.just(path)
            .map(new Func1<String, File[]>() {
                @Override
                public File[] call(String rootPath) {
                    File filePath = new File(rootPath);
                    if (filePath.exists() && filePath.isDirectory()) {
                        return filePath.listFiles();
                    }
                    return null;
                }
            })
            .filter(new Func1<File[], Boolean>() {
                @Override
                public Boolean call(File[] files) {
                    return files != null;
                }
            })
            .map(new Func1<File[], List<CoverItem>>() {
                @Override
                public List<CoverItem> call(File[] groups) {
                    List<CoverItem> res = new ArrayList<>();
                    for (File group : groups) {
                        if (Const.DOWNLOAD_TEMP_NAME.equals(group.getName())) {
                            continue;
                        }
                        File[] pics = group.listFiles();
                        if (pics != null && pics.length > 0) {
                            //默认取0.kmp，如果没有，则取第0个
                            File cover = new File(group, DOWNLOAD_FIRST_IMAGE_NAME + FILE_END);
                            if (!cover.exists()) {
                                cover = pics[0];
                            }
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(cover.getPath(), options);

                            CoverItem coverItem = new CoverItem();
                            coverItem.setDirectory(group);
                            coverItem.setCoverFile(cover);
                            coverItem.setImgOriginalWidth(options.outWidth);
                            coverItem.setImgOriginalHeight(options.outHeight);
                            coverItem.setRatio(options.outWidth / (float) options.outHeight);

                            res.add(coverItem);
                        } else {
                            res.add(CoverItem.getDefault(group));
                        }
                    }
                    Collections.sort(res);
                    return res;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<CoverItem>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    listener.onError(e.getMessage());
                }

                @Override
                public void onNext(List<CoverItem> coverItems) {
                    listener.onFinish(coverItems);
                }
            });
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static int[] getSize(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        int[] res = new int[2];
        if (bitmap != null) {
            res[0] = bitmap.getWidth();
            res[1] = bitmap.getHeight();
        }
        return res;
    }

    public static boolean checkDirectory(String dirName) {
        File file = new File(Const.PATH, dirName);
        if (!file.exists()) {
            file.mkdirs();
            return true;
        }
        return false;
    }

    /**
     * 保存文件
     *
     * @param responseBody
     * @param fileToSaveName   要保存的位置
     */
    public static void saveResponseToFile(@NonNull ResponseBody responseBody, String parent, @NonNull String fileToSaveName) {
        fileToSaveName += FILE_END;
        final File targetFile = new File(Const.PATH + "/" + parent, fileToSaveName);
        if (targetFile.exists()) {
            targetFile.delete();
        }

        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            fos = new FileOutputStream(targetFile);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readUrlFile(String filePath, final FileReadListener fileReadListener) {
        Observable.just(filePath)
            .map(new Func1<String, List<String>>() {
                @Override
                public List<String> call(String s) {
                    List<String> res = new ArrayList<>();

                    FileInputStream inputStream = null;
                    BufferedReader bufferedReader = null;
                    try {
                        //BufferedReader是可以按行读取文件
                        inputStream = new FileInputStream(s);
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        String str = null;
                        while((str = bufferedReader.readLine()) != null) {
                            res.add(str);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //close
                        try {
                            inputStream.close();
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return res;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<String>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<String> urls) {
                    if (fileReadListener != null) {
                        fileReadListener.onFinish(urls);
                    }
                }
            });
    }

    public static int purge(String path) {
        int count = 0;
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] pages = file.listFiles();
            for (File page : pages) {
                try {
                    String decrypt1 = DesUtil.decrypt(page.getName());
                    String decrypt2 = DesUtil.decrypt(decrypt1);

                    count++;
                    deleteDir(page);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }

    public static File getDownloadedDirFromTemp() {
        File tempDir = new File(Const.DOWNLOAD_TEMP_PATH);
        if (!tempDir.exists()) {
            tempDir.mkdir();
            return null;
        }
        File[] children = tempDir.listFiles();
        if (children == null || children.length != 1) {
            return null;
        }
        return children[0];
    }

    private static void printDate(List<CoverItem> list) {
        if (list == null) {
            return;
        }
        for (CoverItem coverItem : list) {
            System.out.println(coverItem.getPublishDate());
        }
    }

    public interface GetCoverListener {
        void onFinish(List<CoverItem> coverItemList);
        void onError(String msg);
    }

    public interface FileReadListener {
        void onFinish(List<String> urls);
    }
}
