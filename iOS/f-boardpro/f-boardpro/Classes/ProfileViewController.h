//
//  ProfileViewController.h
//  SwitchUserSample
//
//  Created by GBS-ios on 4/25/15.
//
//

#import <UIKit/UIKit.h>
#import "UIImageView+WebCache.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
@interface ProfileViewController : UIViewController<UITableViewDataSource,UITableViewDelegate,UICollectionViewDelegate,UICollectionViewDataSource>{
    UITableView *profileTableView;
    UICollectionView *photoCollectionView;

}
@property(nonatomic,strong)NSString *FBid;
@property(nonatomic,strong)UIView *profileView;
@property(nonatomic,strong)UIScrollView  *scrollView;
@property(nonatomic,strong)UIImageView *coverImageview;
@property(nonatomic,strong)FBSDKProfilePictureView *profileImageView;
@property(nonatomic,strong)UILabel *nameLabel;
@property(nonatomic,strong)NSMutableArray *photoIdArray ;
@property(nonatomic,strong)NSMutableArray *pictureArray ;
@property(nonatomic,strong) UIImageView *imageView;
@property(nonatomic,strong)NSString *nameString;
@property(nonatomic,strong)NSString *locationString ;
@property(nonatomic,strong)NSString *homeTownString;
@property(nonatomic,strong)NSString *workString;
@property(nonatomic,strong)NSString *birthdayString;
@property(nonatomic,strong)NSString *coverString;
@property(nonatomic,strong)UIButton *cancelButton;








@end
