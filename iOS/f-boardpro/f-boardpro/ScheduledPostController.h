//
//  ScheduledPostController.h
//  f-boardpro
//

//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <sqlite3.h>

@interface ScheduledPostController : UIViewController<UITableViewDataSource,UITableViewDelegate,UITextViewDelegate>
{
sqlite3 *_databaseHandle;
    UITableView *groupTableView;

}

@property(nonatomic,strong)NSString * accesstokenString;
@property(nonatomic,strong)NSMutableArray *dataArray;
@property(nonatomic,strong)NSMutableArray *fbIDArray;
@property(nonatomic,strong)NSMutableArray *TextArray;
@end
