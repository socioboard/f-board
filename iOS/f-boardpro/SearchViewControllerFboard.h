//
//  SearchViewController.h
//  f-boardpro
//
//  Created by Sumit Ghosh on 14/09/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableCustomCellFboard.h"
#import <FacebookSDK/FacebookSDK.h>
#import "PaggeInformationViewControllerFboard.h"
#import "UIImageView+WebCache.h"
#import "EventinformationViewControllerFboard.h"

@interface SearchViewControllerFboard : UIViewController<UIScrollViewDelegate,UITextFieldDelegate,UITableViewDelegate,UITableViewDataSource>


{
    UIScrollView *searchScrollerView,*scrollViewPaging;
    UIPageControl * pageController;
    UITextField *searchTextField;
    UIButton *searchButton;
    UIImageView *profileImage;
    UITableView *pageTableView;
    UITableView *groupTableView;
    UITableView *eventTableView;
    UITableView *peopleTableView;
    UITableView *placeTableView;
    NSString * searchQuery;
    NSArray * groupData;
    NSArray *eventData;
    NSArray *peopleData;
    NSArray *placeData;
    int pageNo;
}

@property(nonatomic,strong)UITextField *pageTextField;
@property(nonatomic,strong)NSMutableArray *categoryArray;
@property(nonatomic,strong)NSMutableArray *nameArray;
@property(nonatomic,strong)NSMutableArray *idArray;
@property(nonatomic,strong)NSMutableArray *feedDictArray;

@property(nonatomic,strong) PaggeInformationViewControllerFboard *disp;


// For group

@property(nonatomic,strong)NSMutableArray *admistratorArray;
@property(nonatomic,strong)NSMutableArray *grpIdArray;
@property(nonatomic,strong)NSMutableArray *grpNameArray;
@property(nonatomic,strong)NSMutableArray *unreadArray;


// For Events


@property(nonatomic,strong)NSMutableArray *eventadmistratorArray;
@property(nonatomic,strong)NSMutableArray *eventIdArray;
@property(nonatomic,strong)NSMutableArray *eventNameArray;
@property(nonatomic,strong)NSMutableArray *eventunreadArray;

@property(nonatomic,strong) EventinformationViewControllerFboard *dis;


@end
