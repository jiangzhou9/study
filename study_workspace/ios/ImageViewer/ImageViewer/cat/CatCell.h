//
//  CatCell.h
//  ImageViewer
//
//  Created by YangQiang on 2019/3/17.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CatCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgCat;

@property (weak, nonatomic) IBOutlet UILabel *numLabel;

@end

NS_ASSUME_NONNULL_END
