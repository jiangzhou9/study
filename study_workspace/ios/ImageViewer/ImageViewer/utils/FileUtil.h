//
//  FileUtil.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Const.h"
#import "CoverItem.h"
#import "DetailItem.h"

#define PATH_DOCUMENTS [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject]
#define PATH_LIBRARY [NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES) lastObject]

@interface FileUtil : NSObject

+ (void) moveAll;
+ (void) moveOneToInner;
+ (NSArray<CoverItem *> *) getCoverList;
+ (NSArray<DetailItem *> *) getImgInGroup :(NSString *)groupPath;

+ (BOOL) rename :(NSString *)path newName:(NSString *)newName;
+ (BOOL) newFolder :(NSString *)path;
+ (BOOL) newFolder :(NSString *)path newName:(NSString *) newName;
+ (void) copyFile :(NSString *)source target:(NSString *)target;
+ (BOOL) moveFile :(NSString *)source targetDir:(NSString *)targetDir;
+ (BOOL) delete :(NSString *)path;
@end
