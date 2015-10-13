//
//  EventinformationViewController.h
//  f-boardpro
//
//  Created by Sumit Ghosh on 18/09/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableCustomCellFboard.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"
@interface EventinformationViewControllerFboard : UIViewController<UITableViewDataSource,UITableViewDelegate>{
    UITableView *eventTableView;
    UIToolbar *toolBar1;
    NSURL * bannerImageUrl;
    NSArray * allLikesInPage;
    NSArray * allCommentInpage;
    NSArray * allSharesInpPage;
}


@property(nonatomic,strong)NSString *eventidStr;
@property(nonatomic,strong)NSMutableArray *eventadmistratorArray;
@property(nonatomic,strong)NSMutableArray *eventIdArray;
@property(nonatomic,strong)NSMutableArray *eventNameArray;
@property(nonatomic,strong)NSMutableArray *eventunreadArray;
@property(nonatomic,strong)UIImageView *coverImageview;
@property(nonatomic,strong)NSString *coverString;
@property(nonatomic,strong)NSMutableArray *descArray;
@property(nonatomic,strong) NSMutableArray *msgArray;
@property (nonatomic,strong)NSMutableArray *pictureArray;
@property(nonatomic,strong)NSArray * allDataOfEventDetail;

@end
