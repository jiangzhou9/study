//
//  CatImg.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CatImg : NSObject
@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) UIImage *uiImage;
@property (nonatomic, strong) NSValue *size;
@end

NS_ASSUME_NONNULL_END
