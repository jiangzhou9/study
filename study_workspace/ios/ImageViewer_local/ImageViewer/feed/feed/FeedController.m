//
//  FeedViewController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/6.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "FeedController.h"

#define CELL_IDENTIFIER @"FeedViewCell"

@interface FeedController () <UICollectionViewDataSource, CHTCollectionViewDelegateWaterfallLayout, UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UICollectionView *feedCollectView;
@property (weak, nonatomic) IBOutlet UITextField *searchBox;

@property (nonatomic, strong) NSArray<CoverItem *> *fullCoverArray;
@property (nonatomic, strong) NSMutableArray<CoverItem *> *listCoverArray;

@end

@implementation FeedController
- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.fullCoverArray = [FileUtil getCoverList];
    self.listCoverArray = [NSMutableArray arrayWithArray:self.fullCoverArray];
    
    CHTCollectionViewWaterfallLayout *layout = (CHTCollectionViewWaterfallLayout *)_feedCollectView.collectionViewLayout;
    layout.sectionInset = UIEdgeInsetsMake(0/*CGRectGetHeight([ScreenUtil getNotchRect])*/, 0, 0, 0);
    layout.minimumColumnSpacing = 0.5;
    layout.minimumInteritemSpacing = 0.5;
    layout.columnCount = 3;
    
    _feedCollectView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;//避免顶部空白
    _feedCollectView.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
    _feedCollectView.dataSource = self;
    _feedCollectView.delegate = self;
    _feedCollectView.backgroundColor = [UIColor whiteColor];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
//    [self updateLayoutForOrientation:[UIApplication sharedApplication].statusBarOrientation];
}
//
//- (void)updateLayoutForOrientation:(UIInterfaceOrientation)orientation {
//    CHTCollectionViewWaterfallLayout *layout =
//    (CHTCollectionViewWaterfallLayout *)self.feedCollectView.collectionViewLayout;
////    layout.columnCount = 3;
//}

#pragma mark - UICollectionViewDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return [self.listCoverArray count];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    FeedViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:CELL_IDENTIFIER
                                                                   forIndexPath:indexPath];
    
    cell.imgCover.image = [self gettUIImage:indexPath.item];
    cell.labelTitle.text = [self.listCoverArray objectAtIndex:indexPath.item].title;
    cell.labelDate.text = [self.listCoverArray objectAtIndex:indexPath.item].publishDate;
    cell.labelCount.text = [NSString stringWithFormat:@"%d张", [self.listCoverArray objectAtIndex:indexPath.item].imgCount];
    
    if ([[cell.viewGradient.layer sublayers] count] == 0) {
        CAGradientLayer *gradient = [CAGradientLayer layer];
        gradient = [CAGradientLayer layer];
        gradient.frame = cell.viewGradient.bounds;
        gradient.colors = @[(id)[ColorUtil colorWithHexString:@"00000000"].CGColor, (id)[ColorUtil colorWithHexString:@"77181820"].CGColor];
//        gradient.colors = @[(id)[ColorUtil colorWithHexString:@"77ff0000"].CGColor, (id)[ColorUtil colorWithHexString:@"7700ff00"].CGColor];
        [cell.viewGradient.layer insertSublayer:gradient atIndex:0];
    }
    return cell;
}

#pragma mark - CHTCollectionViewDelegateWaterfallLayout
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    UIImage *img = [self gettUIImage:indexPath.item];
    int width = (SCREEN_WIDTH - 3) / 3;
    int height = width * img.size.height / img.size.width;
    return CGSizeMake(width, height);
}

//这里的sender就是cell对象
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([segue.identifier isEqualToString:@"toDetail"]) {
        //get index
        NSIndexPath *indexPath = [self.feedCollectView indexPathForCell:sender];
        
        DetailListController *receive = segue.destinationViewController;
        FeedViewCell *cell = sender;
        receive.picTitle = cell.labelTitle.text;
        receive.groupPath = [self.listCoverArray objectAtIndex:indexPath.item].groupPath;
    }
}

- (IBAction)onTextChange:(id)sender {
    NSString *content = self.searchBox.text;
    
    if (content.length == 0) {
        self.listCoverArray = [NSMutableArray arrayWithArray:self.fullCoverArray];
    } else {
        [self.listCoverArray removeAllObjects];
        for (CoverItem *item in self.fullCoverArray) {
            if ([item.title rangeOfString:content].location != NSNotFound) {
                [self.listCoverArray addObject:item];
            }
        }
    }
    
    [self.feedCollectView reloadData];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    NSLog(@"textField get focus, press return");
    [textField resignFirstResponder];
    return YES;
}

- (IBAction)onToTopClick:(id)sender {
    [self.feedCollectView setContentOffset:CGPointZero animated:YES];
}

- (IBAction)onRefreshClick:(id)sender {
    self.fullCoverArray = [FileUtil getCoverList];
    self.listCoverArray = [NSMutableArray arrayWithArray:self.fullCoverArray];
    [self.feedCollectView reloadData];
    [self.feedCollectView setContentOffset:CGPointZero animated:YES];
}


- (UIImage *)gettUIImage :(NSUInteger)index {
    return [UIImage imageWithContentsOfFile:[self.listCoverArray objectAtIndex:index].coverImagePath];
}

@end
