//
//  NetworkUtil.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFNetworking.h"
#import "Const.h"
#import "Ono.h"

NS_ASSUME_NONNULL_BEGIN

@interface NetworkUtil : NSObject
+ (void) downloadImage :(NSString *)url;
+ (void) requestHtml :(NSString *)url;
+ (NSMutableArray<NSString *> *) extractImageUrl :(NSString *)htmlStr;
@end

NS_ASSUME_NONNULL_END
