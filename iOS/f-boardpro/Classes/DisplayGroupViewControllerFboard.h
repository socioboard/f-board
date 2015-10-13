//
//  DisplayGroupViewControllerFboard.h
//  TwitterBoard
//
//  Created by GBS-ios on 4/23/15.

//

#import <UIKit/UIKit.h>
#import "GroupViewControllerFboard.h"

@interface DisplayGroupViewControllerFboard : UIViewController<UITableViewDataSource,UITableViewDelegate>{
    UITableView *groupFeedsView;
    
     NSURL * bannerImageUrl;
    NSArray * allLikesInPage;
    NSArray * allCommentInpage;
    NSArray * allSharesInpPage;
}

@property(nonatomic,strong)UIImageView *groupView;
@property(nonatomic,strong)UILabel *descriptionLabel;
@property(nonatomic,strong)UILabel *emailLabel;
@property(nonatomic,strong)NSString *iconUrl;
@property(nonatomic,strong)UILabel * ownerNameLabel;
@property(nonatomic,strong)NSString *desc;
@property(nonatomic,strong)NSString *email;
@property(nonatomic,strong)NSString *ownerName;
@property(nonatomic,strong)NSString *idStr;
@property (nonatomic,strong)NSMutableArray *pictureArray;
@property(nonatomic,strong) NSMutableArray *msgArray;
@property(nonatomic,strong)NSMutableArray *fromIdArray;
@property(nonatomic,strong)NSMutableArray *fromNameArray;

@property(nonatomic,strong)NSMutableArray *descArray;
@property(nonatomic,strong)UIImageView *coverImageview;
@property(nonatomic,strong)NSString *coverString;
@property(nonatomic,strong)NSMutableArray *feedDictArray;
@property(nonatomic,strong)NSMutableArray *feedsArray;
@property(nonatomic,strong)NSMutableArray *dicArray;

@end
