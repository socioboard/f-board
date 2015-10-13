//
//  ProfileViewController.h
//  TwitterBoard
//
//  Created by GLB-254 on 4/18/15.
//  Copyright (c) 2015 Socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TableCustomCellFboard.h"
#import <FacebookSDK/FacebookSDK.h>
#import "PaggeInformationViewControllerFboard.h"
#import "UIImageView+WebCache.h"

@interface PageViewControllerFboard : UIViewController<UITextFieldDelegate,UITableViewDataSource,UITableViewDelegate>{
    UITableView *feedTable;
    UIToolbar *toolBar1;
    UIToolbar *toolBar2;
}

@property(nonatomic,strong)UITextField *pageTextField;
@property(nonatomic,strong)NSMutableArray *categoryArray;
@property(nonatomic,strong)NSMutableArray *nameArray;
@property(nonatomic,strong)NSMutableArray *idArray;
@property(nonatomic,strong)NSMutableArray *feedDictArray;

@property(nonatomic,strong)NSMutableArray *fromIdArray;

@property(nonatomic,strong) PaggeInformationViewControllerFboard *disp;

@end
