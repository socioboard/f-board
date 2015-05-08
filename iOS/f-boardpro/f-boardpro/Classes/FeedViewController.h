//
//  FeedViewController.h
//  SwitchUserSample
//

//
//

#import <UIKit/UIKit.h>

@interface FeedViewController : UIViewController<UITableViewDataSource,UITableViewDelegate>
@property(nonatomic,strong)UITableView *tableView;
@property(nonatomic,strong)NSArray *mutArray;
@end
