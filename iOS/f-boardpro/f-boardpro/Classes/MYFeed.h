//
//  FollwViewController.h
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MYFeed.h"

@interface MYFeed : UIViewController<UITableViewDataSource,UITableViewDelegate>
{
    UITableView * followTableView;
    UIToolbar *toolBar;

   }
@property(nonatomic,strong)NSMutableArray *feedsArray;
@property(nonatomic,strong)NSMutableArray *msgArray;
@property(nonatomic,strong) NSMutableArray *descArray;
@property(nonatomic,strong) NSMutableArray *dicArray;
@property(nonatomic,strong) NSMutableArray *ownerIdArray;
@property(nonatomic,strong) NSMutableArray *ownerNameArray;
@property(nonatomic,strong)NSMutableArray *grpIdArray;
@property(nonatomic,strong)NSMutableArray *feedDictArray;
@property(nonatomic,strong)NSString *pagingString;

@end
