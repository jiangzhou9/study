//
//  DetailListController.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DetailViewCell.h"
#import "Common.h"
#import "Cats.h"
#import "WMPhotoBrowser.h"
#import "UIView+WMFrame.h"

NS_ASSUME_NONNULL_BEGIN

@interface DetailListController : UITableViewController

@property (strong, nonatomic) NSString *picTitle;
@property (strong, nonatomic) NSString *groupPath;

@end

NS_ASSUME_NONNULL_END
