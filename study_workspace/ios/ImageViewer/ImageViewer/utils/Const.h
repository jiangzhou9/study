//
//  Const.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/10.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

#define KEY_IP @"ip"

@interface Const : NSObject
+(NSString *) getBaseUrl;
+(void) saveBaseUrl :(NSString *)ip;

+(NSString *) getTabListUrl;

+(NSString *) getCoverListUrl;
+(NSString *) getBaseImgUrl;

+(NSString *) getDetailListUrl;
@end

NS_ASSUME_NONNULL_END
