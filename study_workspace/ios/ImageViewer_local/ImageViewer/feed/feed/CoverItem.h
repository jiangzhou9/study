//
//  CoverItem.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/10.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CoverItem : NSObject

@property(nonatomic, strong) NSString *title;
@property(nonatomic, strong) NSString *publishDate;
@property(nonatomic, strong) NSString *coverImagePath;
@property(nonatomic, strong) NSString *groupPath;
@property(nonatomic, assign) int imgCount;

@end

NS_ASSUME_NONNULL_END
