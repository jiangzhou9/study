//
//  Cats.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "Cats.h"

@implementation Cats
+ (NSMutableArray<CatImg *> *) getCats {
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:[[Cats catsName] count]];
    for (NSString *imgStr in [Cats catsName]) {
        CatImg *catImag = [CatImg new];
        catImag.name = imgStr;
        catImag.uiImage = [UIImage imageNamed:imgStr];
        catImag.size = [NSValue valueWithCGSize:CGSizeMake(catImag.uiImage.size.width, catImag.uiImage.size.height)];
        
        [arr addObject:catImag];
    }
    return arr;
}

+ (NSArray *)catsName {
    return @[@"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg",
                  @"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg",
                  @"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg",
                  @"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg",
                  @"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg",
                  @"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg",
                  @"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg",
                  @"cat1.jpg", @"cat2.jpg", @"cat3.jpg", @"cat4.jpg"];
}
@end
