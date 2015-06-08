//
//  AccountViewController.m
//  f-boardpro
//

//  Copyright (c) 2015 socioboard. All rights reserved.
//
#import "SingletonClass.h"
#import "AccountViewController.h"
#import "ScheduledPostController.h"
#import "CustomMenuViewController.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>
#import "SUCache.h"
#import "AppDelegate.h"
#import "ProfileViewController.h"
#import "SUProfileTableViewCell.h"
#import "ProfileTableViewCell.h"
#import "InviteFriendController.h"
#import "MYFeed.h"
#import "PageViewController.h"
#import "PostScheduleController.h"
#import "PAPPhotoTimelineViewController.h"
#import "PostViewController.h"
#import "GroupViewController.h"
#import "FBAutomationViewController.h"

@interface AccountViewController ()

@end

@implementation AccountViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    if (IS_IPHONE_4_OR_LESS) {
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view.png"]];
    }else if(IS_IPHONE_5){
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view_iphone5.png"]];
    }else if(IS_IPHONE_6){
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view_iphone6@2x.png"]];
    }else if(IS_IPHONE_6P){
        self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg_view@3x.png"]];
    }
    
    UIButton *createAccount = [UIButton buttonWithType:UIButtonTypeCustom];
    createAccount.frame = CGRectMake(self.view.frame.size.width/2-100, self.view.frame.size.height/2, 200, 35);
    createAccount.titleLabel.font = [UIFont systemFontOfSize:9.0f];
    [createAccount setImage:[UIImage imageNamed:@"connect_withfb.png"] forState:UIControlStateNormal];
    createAccount.titleLabel.shadowOffset = CGSizeMake(0.0f, 0.0f);
    
        //self.menuButton.titleLabel.layer.
    [createAccount addTarget:self action:@selector(createNewAccount) forControlEvents:UIControlEventTouchUpInside];
    [createAccount setTitle:@"CreateAccount"  forState:UIControlStateNormal];
    [self.view addSubview:createAccount];
    
    static const int kNumSlots =10;
    BOOL foundToken = NO;
    for (int i = 0; i < kNumSlots; i++) {
        SUCacheItem *item = [SUCache itemForSlot:i];
        if ([item.token isEqualToAccessToken:[FBSDKAccessToken currentAccessToken]]) {
            foundToken = YES;
            break;
        }
    }
    if (!foundToken) {
            // Notably, this makes sure tableView:cellForRowAtIndexPath: doesn't flag a wrong cell.
            //  as selected.
            // Alternatively, we could have found an empty slot to save the "active token".
        [self _deselectRow];
    }
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_accessTokenChange:) name:FBSDKAccessTokenDidChangeNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_currentProfileChange:) name:FBSDKProfileDidChangeNotification object:nil];
    // Do any additional setup after loading the view.
}

- (void)_accessTokenChange:(NSNotification *)notification
{
    FBSDKAccessToken *token = notification.userInfo[FBSDKAccessTokenChangeNewKey];
    
    if (!token) {
        [self _deselectRow];
    } else {
        
        NSInteger slot =[SingletonClass sharedState].selectedUserIndex.row;
        NSLog(@"Accounts Token change %ld",(long)slot);
        SUCacheItem *item = [SUCache itemForSlot:slot] ?: [[SUCacheItem alloc] init];
        if (![item.token isEqualToAccessToken:token]) {
            item.token = token;
            [SUCache saveItem:item slot:slot];
            
        }
    }
}

- (void)_deselectRow
{
        //    [self.tableView cellForRowAtIndexPath:_currentIndexPath].accessoryType = UITableViewCellAccessoryNone;
        //    _currentIndexPath = nil;
    [FBSDKAccessToken setCurrentAccessToken:nil];
    [FBSDKProfile setCurrentProfile:nil];
}

    // The profile information has changed, update the cell and cache.
- (void)_currentProfileChange:(NSNotification *)notification
{
    NSInteger slot = [SingletonClass sharedState].selectedUserIndex.row;
    NSLog(@"profile  change %ld",(long)slot);
    FBSDKProfile *profile = notification.userInfo[FBSDKProfileChangeNewKey];
    if (profile) {
        SUCacheItem *cacheItem = [SUCache itemForSlot:slot];
        cacheItem.profile = profile;
        [SUCache saveItem:cacheItem slot:slot];
        
    }
}

-(void)createNewAccount{

// NSArray *permissions =  [[NSArray alloc] initWithObjects:@"publish_actions", @"user_likes", nil];
    NSArray *permissions =  [[NSArray alloc] initWithObjects:
                             @"user_birthday",
                             @"user_work_history",
                             @"user_likes",
                             @"user_posts",
                             @"user_hometown",
                             @"user_photos",
                             @"user_about_me",
                             @"public_profile",
                             @"user_location",
                             @"user_friends",
                             @"email",
                             nil];
    
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
        login.loginBehavior = FBSDKLoginBehaviorWeb;
    
     [login logInWithReadPermissions:permissions handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
         if (error || result.isCancelled) {
             
   NSLog(@"result grand permission - %@  declinedPermissions - %@",result.grantedPermissions,result.declinedPermissions);
             
         }else {
           NSLog(@"Login permission granted");
            [[NSUserDefaults standardUserDefaults]setInteger:1 forKey:@"LoggedInUser"];
            [[NSUserDefaults standardUserDefaults]synchronize];
            
            MYFeed *follow = [[MYFeed alloc]init];
            follow.title=@"My Feeds";
            NSLog(@"Title =- %@",follow.title);
            
            PAPPhotoTimelineViewController *unfollow=[[PAPPhotoTimelineViewController alloc] initWithStyle:UITableViewStylePlain];
            unfollow.title=@"Home Feeds";
            
            PageViewController *profile = [[PageViewController alloc] init];
            profile.title  = @"Pages";
            
            PostViewController *post = [[PostViewController alloc] init];
            post.title = @"Post schedule";
            
            UICollectionViewFlowLayout *layout=[[UICollectionViewFlowLayout alloc] init];
            [layout setScrollDirection:UICollectionViewScrollDirectionVertical];
            [layout setItemSize:CGSizeMake(150, 50)];
            
            FBAutomationViewController *automaton=[[FBAutomationViewController alloc] initWithCollectionViewLayout:layout];
            automaton.title=@"Friends";
            
            ProfileViewController *profileView=[[ProfileViewController alloc] init];
            profileView.title=@"Profile";

             InviteFriendController *inviteFriend=[[InviteFriendController alloc]init];
             inviteFriend.title=@"Invite Friend";

             
            [[NSNotificationCenter defaultCenter]removeObserver:self];
            CustomMenuViewController *customMenu=[[CustomMenuViewController alloc] init];
            customMenu.viewControllers = @[follow,unfollow,profile,post,automaton,profileView,inviteFriend];
            self.navigationController.navigationBarHidden=YES;
           [self.navigationController pushViewController:customMenu animated:YES];
            }
    }]; 
}

-(void)dealloc{
    [[NSNotificationCenter defaultCenter]removeObserver:self];
}

-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:YES];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:FBSDKAccessTokenDidChangeNotification object:nil];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:FBSDKProfileDidChangeNotification object:nil];

}

-(void)viewWillAppear:(BOOL)animated{

    [super viewWillAppear:YES];
     self.navigationController.navigationBarHidden=YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
