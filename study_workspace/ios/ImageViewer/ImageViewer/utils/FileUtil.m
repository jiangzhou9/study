//
//  FileUtil.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "FileUtil.h"

@implementation FileUtil {
    
}

//dirs in shared folder copy to inner
+ (void) moveAll {
    NSString *source = [PATH_LIBRARY stringByAppendingPathComponent:Const.getRootFolderName];
    NSString *target = PATH_DOCUMENTS;
    
    [self moveFile:source targetDir:target];
}

+ (void) moveOneToInner {
    [self deleteDSStoreFileInSingle];
    
    NSString *target = [PATH_LIBRARY stringByAppendingPathComponent:Const.getRootFolderName];
    NSString *root = PATH_DOCUMENTS;
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSArray* newGroups = [fileManager contentsOfDirectoryAtPath:root error:nil];
    for(int i = 0; i<[newGroups count]; i++) {
        NSString *source = [root stringByAppendingPathComponent:[newGroups objectAtIndex:i]];
        [self moveFile:source targetDir:target];
    }
}

+ (NSArray<CoverItem *> *) getCoverList {
    [self deleteDSStoreFileInAll];
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *source = [PATH_DOCUMENTS stringByAppendingPathComponent:Const.getRootFolderName];
    NSArray* groups = [fileManager contentsOfDirectoryAtPath:source error:nil];
    
    NSMutableArray *res = [NSMutableArray new];
    for(int i = 0; i<[groups count]; i++) {
        //only name, without path
        NSString *folderName = [groups objectAtIndex:i];
        if ([folderName isEqualToString:@".DS_Store"]) {
            continue;
        }
        NSString *folderPath = [source stringByAppendingPathComponent:[groups objectAtIndex:i]];//lastPathComponent is foldname
        
        CoverItem *coverItem = [CoverItem new];
        coverItem.groupPath = folderPath;
        
        NSArray *nameAndDate = [folderName componentsSeparatedByString:@"_"];
        if ([nameAndDate count] == 1) {
            coverItem.title = [nameAndDate objectAtIndex:0];
        } else if ([nameAndDate count] == 2) {
            coverItem.title = [nameAndDate objectAtIndex:0];
            coverItem.publishDate = [nameAndDate objectAtIndex:1];
        }
        //the first one may be 8.png
        NSArray* imgs = [fileManager contentsOfDirectoryAtPath:folderPath error:nil];
        for(int i = 0; i<[imgs count]; i++) {
            NSString *imgName1 = [NSString stringWithFormat:@"%d.jpg", i];
            NSString *imgName2 = [NSString stringWithFormat:@"%d.JPG", i];
            NSString *imgName3 = [NSString stringWithFormat:@"%d.png", i];
            NSString *imgName4 = [NSString stringWithFormat:@"%d.PNG", i];
            NSString *checkPath1 = [folderPath stringByAppendingPathComponent:imgName1];
            NSString *checkPath2 = [folderPath stringByAppendingPathComponent:imgName2];
            NSString *checkPath3 = [folderPath stringByAppendingPathComponent:imgName3];
            NSString *checkPath4 = [folderPath stringByAppendingPathComponent:imgName4];
            if ([fileManager fileExistsAtPath:checkPath1]) {
                coverItem.coverImagePath = checkPath1;
                break;
            }
            if ([fileManager fileExistsAtPath:checkPath2]) {
                coverItem.coverImagePath = checkPath2;
                break;
            }
            if ([fileManager fileExistsAtPath:checkPath3]) {
                coverItem.coverImagePath = checkPath3;
                break;
            }
            if ([fileManager fileExistsAtPath:checkPath4]) {
                coverItem.coverImagePath = checkPath4;
                break;
            }
        }
        if (coverItem.coverImagePath.length == 0) {
            coverItem.coverImagePath = [folderPath stringByAppendingPathComponent:[[fileManager contentsOfDirectoryAtPath:folderPath error:nil] objectAtIndex:0]];
        }
        
//        NSLog(@"coverpath:%@", coverItem.coverImagePath);
        coverItem.imgCount = (int)[[fileManager contentsOfDirectoryAtPath:folderPath error:nil] count];
        
        [res addObject:coverItem];
    }
    return [res sortedArrayUsingSelector:@selector(compare:)];
}

+ (void) deleteDSStoreFileInAll {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *source = [PATH_DOCUMENTS stringByAppendingPathComponent:Const.getRootFolderName];
    NSArray* groups = [fileManager contentsOfDirectoryAtPath:source error:nil];
    for(int i = 0; i<[groups count]; i++) {
        //only name, without path
        NSString *folderName = [groups objectAtIndex:i];
        NSString *folderPath = [source stringByAppendingPathComponent:[groups objectAtIndex:i]];//lastPathComponent is foldname
        if ([folderName isEqualToString:@".DS_Store"]) {
            [self delete:folderPath];
            continue;
        }
        
        NSArray* images = [fileManager contentsOfDirectoryAtPath:folderPath error:nil];
        for(int i = 0; i<[images count]; i++) {
            NSString *imageName = [images objectAtIndex:i];
            NSString *imagePath = [folderPath stringByAppendingPathComponent:[images objectAtIndex:i]];//lastPathComponent is foldname
            if ([imageName isEqualToString:@".DS_Store"]) {
                [self delete:imagePath];
                continue;
            }
        }
    }
}

+ (void) deleteDSStoreFileInSingle {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *source = PATH_DOCUMENTS;
    NSArray* groups = [fileManager contentsOfDirectoryAtPath:source error:nil];
    for(int i = 0; i<[groups count]; i++) {
        //only name, without path
        NSString *folderName = [groups objectAtIndex:i];
        NSString *folderPath = [source stringByAppendingPathComponent:[groups objectAtIndex:i]];//lastPathComponent is foldname
        if ([folderName isEqualToString:@".DS_Store"]) {
            [self delete:folderPath];
            continue;
        }
        
        NSArray* images = [fileManager contentsOfDirectoryAtPath:folderPath error:nil];
        for(int i = 0; i<[images count]; i++) {
            NSString *imageName = [images objectAtIndex:i];
            NSString *imagePath = [folderPath stringByAppendingPathComponent:[images objectAtIndex:i]];//lastPathComponent is foldname
            if ([imageName isEqualToString:@".DS_Store"]) {
                [self delete:imagePath];
                continue;
            }
        }
    }
}

+ (NSArray<DetailItem *> *) getImgInGroup :(NSString *)groupPath {
    NSMutableArray *res = [NSMutableArray new];
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSArray* groups = [fileManager contentsOfDirectoryAtPath:groupPath error:nil];
    for(int i = 0; i<[groups count]; i++) {
        DetailItem *detailItem = [DetailItem new];
        detailItem.imgPath = [groupPath stringByAppendingPathComponent:[groups objectAtIndex:i]];
        detailItem.imgName = [groups objectAtIndex:i];
        [res addObject:detailItem];
    }
    NSArray *a =[res sortedArrayUsingSelector:@selector(compare:)];
    return a;
}



//both dir and file
+ (BOOL) rename :(NSString *)path newName:(NSString *)newName {
    //通过移动该文件对文件重命名
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath:path]) {
        return NO;
    }
    NSString *newPath = [[path stringByDeletingLastPathComponent] stringByAppendingPathComponent:newName];
    NSError *err = nil;
    BOOL res = [fileManager moveItemAtPath:path toPath:newPath error:&err];
    NSLog(@"moveFile %@, err:%@", res ? @"yes" : @"no", err);
    return res;
}

//直接创建path
+ (BOOL) newFolder :(NSString *)path {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if (![fileManager fileExistsAtPath:path]) {
        return [fileManager createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
    } else {
        return NO;
    }
}

//在path下创建newName
+ (BOOL) newFolder :(NSString *)path newName:(NSString *) newName {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *parentPath = [path stringByAppendingPathComponent:newName];
    if (![fileManager fileExistsAtPath:parentPath]) {
        return [fileManager createDirectoryAtPath:parentPath withIntermediateDirectories:YES attributes:nil error:nil];
    } else {
        return NO;
    }
}

//如果都是文件夹，把source下的都考到target，不包括source自己
+  (void) copyFile :(NSString *)source target:(NSString *)target {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    
    NSArray* sourceArray = [fileManager contentsOfDirectoryAtPath:source error:nil];
    for(int i = 0; i<[sourceArray count]; i++) {
        NSString *sourcePath = [source stringByAppendingPathComponent:[sourceArray objectAtIndex:i]];
        NSString *targetPath = [target stringByAppendingPathComponent:[sourceArray objectAtIndex:i]];
        NSLog(@"sourcePath: %@",sourcePath);
        NSLog(@"targetPath: %@",targetPath);
        NSLog(@"----------------------------");
        
        //判断是不是文件夹
        BOOL isFolder = NO;
        //判断是不是存在路径 并且是不是文件夹
        BOOL isExist = [fileManager fileExistsAtPath:sourcePath isDirectory:&isFolder];
        if (isExist) {
            NSError *err = nil;
            BOOL res = [fileManager copyItemAtPath:sourcePath toPath:targetPath error:&err];
            NSLog(@"copyItemAtPath %@, err:%@", res ? @"yes" : @"no", err);
        }
    }
}

//把source（包括source）移动到targetDir下面
+ (BOOL) moveFile :(NSString *)source targetDir:(NSString *)targetDir {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    
    [self listFiles:[source stringByDeletingLastPathComponent]];
    
    NSString *fileName = [source lastPathComponent];
    NSString *targetPath = [targetDir stringByAppendingPathComponent:fileName];
    if (![fileManager fileExistsAtPath:source]) {
        return NO;
    }
    
    if (![fileManager fileExistsAtPath:targetDir]) {
        [self newFolder:targetDir];
    }
    
    NSError *err = nil;
    BOOL res = [fileManager moveItemAtPath:source toPath:targetPath error:&err];
    NSLog(@"moveFile %@, err:%@", res ? @"yes" : @"no", err);
    return res;
}
//both dir and file
+ (BOOL) delete :(NSString *)path {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if ([fileManager fileExistsAtPath:path]) {
        NSError *err = nil;
        BOOL res = [fileManager removeItemAtPath:path error:&err];
        NSLog(@"delete %@, err:%@", res ? @"yes" : @"no", err);
        return res;
    } else {
        return NO;
    }
}

+ (NSArray<NSString *> *) listFiles :(NSString *) path {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    
    NSArray* sourceArray = [fileManager contentsOfDirectoryAtPath:path error:nil];
    NSLog(@"-------------files start: %@", path);
    for(int i = 0; i<[sourceArray count]; i++) {
        NSLog(@"%@", [sourceArray objectAtIndex:i]);
    }
    NSLog(@"-------------files end------------");
    return sourceArray;
}

@end
