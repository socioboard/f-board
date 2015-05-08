//
//  UnfollowViewController.h
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableCustomCell.h"
#import "HomeFeed.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"


@interface HomeFeed : UIViewController<UITableViewDataSource,UITableViewDelegate>{
    UITableView * homeFeedTableView;
}
@property(nonatomic,strong)NSMutableArray *grpIdArray;

@property(nonatomic,strong)NSMutableArray *pictureArray;
@property(nonatomic,strong)NSMutableArray *storyArray;
@property(nonatomic,strong)NSMutableArray *descArray;
@property(nonatomic,strong)NSMutableArray *likeArray;
@property(nonatomic,strong)NSMutableArray *commentArray;
@property(nonatomic,strong)NSString *pagingString;
@end
