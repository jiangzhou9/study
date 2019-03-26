//
//  DetailItem.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/14.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "DetailItem.h"

@implementation DetailItem

- (NSComparisonResult)compare:(DetailItem *)other{
    NSString *name1 = self.imgName;
    NSString *name2 = other.imgName;
    
    if (name1.length == 0 && name2.length != 0) {
        return (NSComparisonResult)NSOrderedDescending;
    } else if (name1.length != 0 && name2.length == 0) {
        return (NSComparisonResult)NSOrderedAscending;
    } else if (name1.length == 0 && name2.length == 0) {
        return (NSComparisonResult)NSOrderedSame;
    }
    
    NSArray *arr1 = [name1 componentsSeparatedByString:@"."];
    NSArray *arr2 = [name2 componentsSeparatedByString:@"."];
    
    int val1 = [[arr1 objectAtIndex:0] intValue];
    int val2 = [[arr2 objectAtIndex:0] intValue];
    if (val1 > val2) {
        return (NSComparisonResult)NSOrderedDescending;
    } else if (val1 < val2) {
        return (NSComparisonResult)NSOrderedAscending;
    }
    
    return (NSComparisonResult)NSOrderedSame;
}

@end
