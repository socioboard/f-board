//
//  FollwViewController.h
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MYFeedFboard.h"
#import "CommentsViewControllerFboard.h"

@interface MYFeedFboard : UIViewController<UITableViewDataSource,UITableViewDelegate>
{
    UITableView * followTableView;
    UIToolbar *toolBar1;
    UIToolbar *toolBar2;

   }
@property(nonatomic,strong) CommentsViewControllerFboard  *photoDetail;
//@property(nonatomic,strong) NSMutableArray *feedsArray;
//@property(nonatomic,strong) NSMutableArray *msgArray;
//@property(nonatomic,strong) NSMutableArray *descArray;
//@property(nonatomic,strong) NSMutableArray *dicArray;
//@property(nonatomic,strong) NSMutableArray *ownerIdArray;
//@property(nonatomic,strong) NSMutableArray *ownerNameArray;
//@property(nonatomic,strong) NSMutableArray *grpIdArray;
//@property(nonatomic,strong) NSMutableArray *feedDictArray;
////Sukhmeet Code
@property(nonatomic,strong) NSArray * feedDataArray;
@property(nonatomic,strong)NSString *pagingString;

@end
