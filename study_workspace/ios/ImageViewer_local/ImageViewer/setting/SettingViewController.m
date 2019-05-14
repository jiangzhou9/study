//
//  SecondViewController.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/5.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "SettingViewController.h"

@interface SettingViewController ()
@property(nonatomic, strong) MBProgressHUD *waitDialog;
@end

@implementation SettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
}

- (IBAction)onCopyToInnerClick:(id)sender {
    self.waitDialog = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:self.waitDialog];
    self.waitDialog.dimBackground = YES;
    self.waitDialog.labelText = @"copying all...";
    [self.waitDialog showAnimated:YES whileExecutingBlock:^{
        //not in main thread
        [FileUtil moveAll];
    } completionBlock:^{
        [self.waitDialog removeFromSuperview];
        self.waitDialog = nil;
    }];
}

- (IBAction)onCopyOneToInner:(id)sender {
    self.waitDialog = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:self.waitDialog];
    self.waitDialog.dimBackground = YES;
    self.waitDialog.labelText = @"copying one...";
    [self.waitDialog showAnimated:YES whileExecutingBlock:^{
        //not in main thread
        [FileUtil moveOneToInner];
    } completionBlock:^{
        [self.waitDialog removeFromSuperview];
        self.waitDialog = nil;
    }];
}


@end
