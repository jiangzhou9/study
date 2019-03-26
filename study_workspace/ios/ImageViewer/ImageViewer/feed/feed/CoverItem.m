//
//  CoverItem.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/10.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "CoverItem.h"

@implementation CoverItem

- (NSComparisonResult)compare:(CoverItem *)other{
    NSString *date1 = self.publishDate;
    NSString *date2 = other.publishDate;
    
    if (date1.length == 0 && date2.length != 0) {
//        NSLog(@"compare null des, 1:%@, 2:%@", date1, date2);
        return (NSComparisonResult)NSOrderedDescending;
    } else if (date1.length != 0 && date2.length == 0) {
//        NSLog(@"compare null asc, 1:%@, 2:%@", date1, date2);
        return (NSComparisonResult)NSOrderedAscending;
    } else if (date1.length == 0 && date2.length == 0) {
//        NSLog(@"compare null same, 1:%@, 2:%@", date1, date2);
        return (NSComparisonResult)NSOrderedSame;
    }
    
    NSArray *arr1 = [date1 componentsSeparatedByString:@"-"];
    NSArray *arr2 = [date2 componentsSeparatedByString:@"-"];
    
    for (int i = 0; i < [arr1 count]; i++) {
        int val1 = [[arr1 objectAtIndex:i] intValue];
        int val2 = [[arr2 objectAtIndex:i] intValue];
        if (val1 > val2) {
//            NSLog(@"compare asc, 1:%@, 2:%@", date1, date2);
            return (NSComparisonResult)NSOrderedAscending;
        } else if (val1 < val2) {
//            NSLog(@"compare des, 1:%@, 2:%@", date1, date2);
            return (NSComparisonResult)NSOrderedDescending;
        }
    }
//    NSLog(@"compare same, 1:%@, 2:%@", date1, date2);
    return (NSComparisonResult)NSOrderedSame;
}
@end
