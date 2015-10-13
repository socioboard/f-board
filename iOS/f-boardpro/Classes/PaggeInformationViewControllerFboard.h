//
//  FeedDisplayViewController.h
//  TwitterBoard
//
//  Created by GBS-ios on 4/24/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableCustomCellFboard.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"
@interface PaggeInformationViewControllerFboard : UIViewController<UITableViewDataSource,UITableViewDelegate>{
    UITableView *feedTableView;
    UIToolbar *toolBar1;
    NSURL * bannerImageUrl;
    NSArray * allLikesInPage;
    NSArray * allCommentInpage;
    NSArray * allSharesInpPage;
}

@property(nonatomic,strong)NSString *idStr;
@property (nonatomic,strong)NSMutableArray *pictureArray;
@property(nonatomic,strong) NSMutableArray *msgArray;
@property(nonatomic,strong)NSMutableArray *fromIdArray;
@property(nonatomic,strong)NSMutableArray *fromNameArray;
@property(nonatomic,strong)NSMutableArray *descArray;
@property(nonatomic,strong)UIImageView *coverImageview;
@property(nonatomic,strong)NSString *coverString;


@property(nonatomic,strong)NSMutableArray *grpIdArray;
@property(nonatomic,strong)NSMutableArray *feedDictArray;
@property(nonatomic,strong)NSMutableArray *feedsArray;
@property(nonatomic,strong)NSMutableArray *dicArray;
@end
