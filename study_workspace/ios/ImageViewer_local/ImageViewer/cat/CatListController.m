//
//  CatListController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/17.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "CatListController.h"

#define CELL_IDENTIFIER @"CatViewCell"

@interface CatListController () <UICollectionViewDataSource, CHTCollectionViewDelegateWaterfallLayout>
@property (weak, nonatomic) IBOutlet UICollectionView *catCollectionView;

@property (nonatomic, strong) NSMutableArray<CatImg *> *catArray;

@property (nonatomic, strong) NSString *psd;
@end

@implementation CatListController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.catArray = [Cats getCats];
    self.psd = @"";
    
    CHTCollectionViewWaterfallLayout *layout = (CHTCollectionViewWaterfallLayout *)_catCollectionView.collectionViewLayout;
    layout.sectionInset = UIEdgeInsetsMake(CGRectGetHeight([ScreenUtil getNotchRect]), 0, 0, 0);
    layout.minimumColumnSpacing = 0.5;
    layout.minimumInteritemSpacing = 0.5;
    
    _catCollectionView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;//避免顶部空白
    _catCollectionView.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    _catCollectionView.dataSource = self;
    _catCollectionView.delegate = self;
    _catCollectionView.backgroundColor = [UIColor whiteColor];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self updateLayoutForOrientation:[UIApplication sharedApplication].statusBarOrientation];
}

- (void)updateLayoutForOrientation:(UIInterfaceOrientation)orientation {
    CHTCollectionViewWaterfallLayout *layout =
    (CHTCollectionViewWaterfallLayout *)self.catCollectionView.collectionViewLayout;
    layout.columnCount = 2;
}

#pragma mark - UICollectionViewDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return [self.catArray count];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CatCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:CELL_IDENTIFIER
                                                                   forIndexPath:indexPath];
    cell.imgCat.image = [self.catArray objectAtIndex:indexPath.item].uiImage;
    cell.numLabel.text = [NSString stringWithFormat:@"%d", (int)indexPath.item];
    
    return cell;
}

#pragma mark - CHTCollectionViewDelegateWaterfallLayout
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    UIImage *img = [self gettUIImage:indexPath.item];
    int width = (SCREEN_WIDTH - 2) / 2;
    int height = width * img.size.height / img.size.width;
    return CGSizeMake(width, height);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    self.psd = [self.psd stringByAppendingString:[NSString stringWithFormat:@"%d", (int)indexPath.item]];
    if ([self.psd containsString:[Const getPsd]]) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        UINavigationController *viewController = [storyboard instantiateViewControllerWithIdentifier:@"FeedNavId"];
        [self presentViewController:viewController animated:YES completion:nil];
    }
}

- (UIImage *)gettUIImage :(NSUInteger)index {
    return [self.catArray objectAtIndex:index].uiImage;
}


@end
