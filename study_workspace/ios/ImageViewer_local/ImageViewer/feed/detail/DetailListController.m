//
//  DetailListController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "DetailListController.h"

#define CELL_IDENTIFIER @"DetailViewCell"

@interface DetailListController () <UITableViewDataSource, UITableViewDelegate>
@property (strong, nonatomic) IBOutlet UITableView *imgTableView;

@property (nonatomic, strong) NSArray<DetailItem *> *imgArray;

@end

@implementation DetailListController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.imgArray = [FileUtil getImgInGroup:self.groupPath];
}

#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.imgArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    DetailViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CELL_IDENTIFIER forIndexPath:indexPath];
    cell.img.image = [UIImage imageWithContentsOfFile:[self.imgArray objectAtIndex:indexPath.item].imgPath];
    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return self.picTitle;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    UIImage *img = [UIImage imageWithContentsOfFile:[self.imgArray objectAtIndex:indexPath.item].imgPath];
    return SCREEN_WIDTH * img.size.height / img.size.width;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    WMPhotoBrowser *browser = [WMPhotoBrowser new];
    
    NSMutableArray *pathArray = [NSMutableArray arrayWithCapacity:[self.imgArray count]];
    for (DetailItem *detailItem in self.imgArray) {
        [pathArray addObject:detailItem.imgPath];
    }
    browser.dataSource = pathArray;
    browser.downLoadNeeded = YES;
    browser.currentPhotoIndex= indexPath.item;
    [self presentViewController:browser animated:YES completion:^{
        
    }];
}

@end
