//
//  Cats.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CatImg.h"
#import "AppDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface Cats : NSObject
+ (NSMutableArray<CatImg *> *) getCats;
@end

NS_ASSUME_NONNULL_END
