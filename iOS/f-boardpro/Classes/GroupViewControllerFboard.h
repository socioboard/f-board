//
//  GroupViewControllerFboard.h
//  TwitterBoard
//
//  Created by GBS-ios on 4/23/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"

@interface GroupViewControllerFboard : UIViewController<UITableViewDataSource,UITableViewDelegate>{
    UITableView *groupTableView;
}

@property(nonatomic,strong)NSMutableArray *admistratorArray;
@property(nonatomic,strong)NSMutableArray *grpIdArray;
@property(nonatomic,strong)NSMutableArray *grpNameArray;
@property(nonatomic,strong)NSMutableArray *unreadArray;

@end
