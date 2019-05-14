//
//  DetailItem.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/14.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "DetailItem.h"

@implementation DetailItem

+(DetailItem *) convert:(NSDictionary *) dic tabIndex: (int) tabIndex {
    DetailItem *res = [DetailItem new];
    for (NSString *key in dic) {
        if ([key isEqualToString:@"img_name"]) {
            res.imgName = dic[key];
        } else if ([key isEqualToString:@"img_path"]) {
            res.imgPath = [NSString stringWithFormat:@"%@/%d/%@", Const.getBaseImgUrl, tabIndex, dic[key]];
        } else if ([key isEqualToString:@"img_width"]) {
            res.imgWidth = [dic[key] floatValue];
        } else if ([key isEqualToString:@"img_height"]) {
            res.imgHeight = [dic[key] intValue];
        }
    }
    return res;
}

@end
