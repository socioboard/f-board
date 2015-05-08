//
//  FBAutomationViewController.h
//  SwitchUserSample
//
//  Created by socioboard  on 4/29/15.
//
//

#import <UIKit/UIKit.h>

@interface FBAutomationViewController : UICollectionViewController
@property(nonatomic,strong)NSMutableArray *mutNowArray;
@property(nonatomic,strong)NSMutableArray *mutOldArray;
@property (nonatomic, strong) UISegmentedControl *segmentControl;
@property (nonatomic,strong)NSMutableArray *unfriendArray;
@property (nonatomic,strong)NSMutableArray *newfriendArray;
@property(nonatomic,strong)UIRefreshControl* refreshControl;

@end
