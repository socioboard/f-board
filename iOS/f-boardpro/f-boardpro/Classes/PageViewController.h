//
//  ProfileViewController.h
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableCustomCell.h"
#import <FacebookSDK/FacebookSDK.h>
#import "FeedDisplayViewController.h"
#import "UIImageView+WebCache.h"

@interface PageViewController : UIViewController<UITextFieldDelegate,UITableViewDataSource,UITableViewDelegate>{
    UITableView *feedTable;
}

@property(nonatomic,strong)UITextField *pageTextField;
@property(nonatomic,strong)NSMutableArray *categoryArray;
@property(nonatomic,strong)NSMutableArray *nameArray;
@property(nonatomic,strong)NSMutableArray *idArray;

@property(nonatomic,strong) FeedDisplayViewController *disp;

@end
