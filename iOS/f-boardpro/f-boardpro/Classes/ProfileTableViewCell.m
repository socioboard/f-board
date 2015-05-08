//
//  ProfileTableViewCell.m
//  SwitchUserSample
//

//
//

#import "ProfileTableViewCell.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>


static const CGFloat leftMargin = 10;
static const CGFloat topMargin = 5;
static const CGFloat rightMargin = 30;
static const CGFloat pictureWidth = 50;
static const CGFloat pictureHeight = 50;

@interface ProfileTableViewCell()

@property (weak, nonatomic) FBSDKProfilePictureView *profilePic;

@end
@implementation ProfileTableViewCell

- (instancetype)init {
    self = [super init];
    if (self) {
        [self initializeSubViews];
    }
    
    return self;
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style
              reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self initializeSubViews];
    }
    return self;
}


- (void)awakeFromNib {
    // Initialization code
    [super awakeFromNib];
    [self initializeSubViews];
    
}

- (void)initializeSubViews {
    FBSDKProfilePictureView *profilePic = [[FBSDKProfilePictureView alloc]
                                           initWithFrame:CGRectMake(
                                                                    leftMargin,
                                                                    topMargin,
                                                                    pictureWidth,
                                                                    pictureHeight)];
//    profilePic.profileID=@"280959705428924";
    [self addSubview:profilePic];
    self.profilePic = profilePic;
    
    self.clipsToBounds = YES;
    self.autoresizesSubviews = YES;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    CGSize size = self.bounds.size;
    
    self.textLabel.frame = CGRectMake(
                                      leftMargin * 2 + pictureWidth,
                                      topMargin,
                                      size.width - leftMargin - pictureWidth - rightMargin,
                                      size.height - topMargin);
}

#pragma mark - Properties

- (NSString *)userID {
    return self.profilePic.profileID;
}

- (void)setUserID:(NSString *)userID {
    self.profilePic.profileID = userID;
}

- (NSString *)userName {
    return self.textLabel.text;
}

- (void)setUserName:(NSString *)userName {
    self.textLabel.text = userName;
}

- (CGFloat)desiredHeight {
    return topMargin * 2 + pictureHeight;
}



- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
