//
//  FeedDisplayViewController.h
//  TwitterBoard
//
//  Created by GBS-ios on 4/24/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableCustomCell.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"
@interface FeedDisplayViewController : UIViewController<UITableViewDataSource,UITableViewDelegate>{
    UITableView *feedTableView;
}

@property(nonatomic,strong)NSString *idStr;
@property (nonatomic,strong)NSMutableArray *pictureArray;
@property(nonatomic,strong) NSMutableArray *msgArray;
@property(nonatomic,strong)NSMutableArray *fromIdArray;
@property(nonatomic,strong)NSMutableArray *fromNameArray;
@property(nonatomic,strong)NSMutableArray *descArray;
@end
