//
//  NetworkUtil.m
//  ImageViewer
//
//  Created by YangQiang on 2019/3/7.
//  Copyright © 2019 YangQiang. All rights reserved.
//

#import "NetworkUtil.h"

@implementation NetworkUtil

+ (void) requestHtml :(NSString *)url {
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    [manager GET:url parameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString *result = [[NSString alloc]initWithData:responseObject encoding:NSUTF8StringEncoding];
        NSLog(@"thread: %@, result: %@", [NSThread isMainThread] ? @"yes" : @"no", result);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", error);
    }];
}

+ (void) downloadImage :(NSString *) url {
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
    NSURLSessionDownloadTask *downloadTask = [manager downloadTaskWithRequest:request progress:nil destination:^NSURL *(NSURL *targetPath, NSURLResponse *response) {
        
        NSURL *documentsDirectoryURL = [[NSFileManager defaultManager] URLForDirectory:NSDocumentDirectory inDomain:NSUserDomainMask appropriateForURL:nil create:NO error:nil];
        
        NSURL *res = [documentsDirectoryURL URLByAppendingPathComponent:[response suggestedFilename]];
        NSLog(@"download, before comp, thread: %@", [NSThread isMainThread] ? @"yes" : @"no");
        return res;
    } completionHandler:^(NSURLResponse *response, NSURL *filePath, NSError *error) {
        NSLog(@"download, in comp, thread: %@, File downloaded to: %@", [NSThread isMainThread] ? @"yes" : @"no", filePath);
        NSData *imgData = [[NSData alloc] initWithContentsOfURL:filePath];
        UIImage *img = [UIImage imageWithData:imgData];
    }];
    [downloadTask resume];
}

//extract all usable imageurl in pageUrl
+ (NSMutableArray<NSString *> *) extractImageUrl :(NSString *)htmlStr {
    NSMutableArray<NSString *> *res = [[NSMutableArray alloc] init];
    
    NSError *err = nil;
    ONOXMLDocument *document = [ONOXMLDocument XMLDocumentWithString:htmlStr encoding:NSUTF8StringEncoding error:&err];
    return res;
}

@end
