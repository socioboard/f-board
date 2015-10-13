//
//  AccountViewController.m
//  f-boardpro
//

//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "AccountViewControllerFboard.h"
#import "ScheduledPostControllerFboard.h"
#import "CustomMenuViewControllerFboard.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>
#import "SUCacheFboard.h"
#import "AppDelegateFboard.h"
#import "ProfileViewControllerFboard.h"
#import "SUProfileTableViewCellFboard.h"

//#import "SUAccountsViewController.h"
#import "MYFeedFboard.h"
#import "PageViewControllerFboard.h"
#import "PostScheduleControllerFboard.h"
#import "FbTimelineViewControllerFboard.h"
#import "PostViewControllerFboard.h"
#import "GroupViewControllerFboard.h"
#import "FbFriendsViewControllerFboard.h"
#import "SearchViewControllerFboard.h"
#import "SingletonClass.h"


@interface AccountViewControllerFboard ()

@end

@implementation AccountViewControllerFboard

- (void)viewDidLoad
{
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
    createAccount.frame = CGRectMake(SCREEN_WIDTH/2-100, self.view.frame.size.height/2,200, 35);
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
        SUCacheItem *item = [SUCacheFboard itemForSlot:i];
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
        
        NSInteger slot =0;
        NSLog(@"Accounts Token change %ld",(long)slot);
        SUCacheItem *item = [SUCacheFboard itemForSlot:slot] ?: [[SUCacheItem alloc] init];
        if (![item.token isEqualToAccessToken:token]) {
            item.token = token;
            [SUCacheFboard saveItem:item slot:slot];
            
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
    NSInteger slot = 0;
    NSLog(@"profile  change %ld",(long)slot);
    FBSDKProfile *profile = notification.userInfo[FBSDKProfileChangeNewKey];
    if (profile) {
        SUCacheItem *cacheItem = [SUCacheFboard itemForSlot:slot];
        cacheItem.profile = profile;
        [SUCacheFboard saveItem:cacheItem slot:slot];
        
    }
}

-(void)createNewAccount{


    
// NSArray *permissions =  [[NSArray alloc] initWithObjects:@"publish_actions", @"user_likes", nil];
    NSArray *permissions =  [[NSArray alloc] initWithObjects:@"contact_email",
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
    
//    [login logInWithPublishPermissions:permissions handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
    
     [login logInWithReadPermissions:permissions handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
         NSLog(@"result = %@",result.description);
        if (result) {
            NSLog(@"Login permission granted");
            [SingletonClass sharedState].accessTokenString=result.token.tokenString;
            [SingletonClass sharedState].fbUserId=result.token.userID;
            [self callExtendedTokenService];
            NSLog(@"Resulted Access Token %@",result.token.tokenString);
            NSLog(@"result grand permission - %@  declinedPermissions - %@",result.grantedPermissions,result.declinedPermissions);
            [[NSUserDefaults standardUserDefaults]setInteger:1 forKey:@"LoggedInUser"];
            [[NSUserDefaults standardUserDefaults]synchronize];
            
            ProfileViewControllerFboard *profileView=[[ProfileViewControllerFboard alloc] init];
            profileView.title=@"Profile";
                    NSLog(@"Title =- %@",profileView.title);
            
            
            
            SearchViewControllerFboard *Search = [[SearchViewControllerFboard alloc]init];
           Search.title=@"Search";
            
            
            
            
            MYFeedFboard *follow = [[MYFeedFboard alloc]init];
            follow.title=@"My Feeds";
          //  NSLog(@"Title =- %@",follow.title);
            
          FbTimelineViewControllerFboard *unfollow=[[FbTimelineViewControllerFboard alloc] initWithStyle:UITableViewStylePlain];
            unfollow.title=@"Home Feeds";
            
            PageViewControllerFboard *profile = [[PageViewControllerFboard alloc] init];
            profile.title  = @"Pages";
            
            PostViewControllerFboard *post = [[PostViewControllerFboard alloc] init];
            post.title = @"Post schedule";
            
            GroupViewControllerFboard *grp = [[GroupViewControllerFboard alloc]init];
            grp.title = @"My Groups";
            UICollectionViewFlowLayout *layout=[[UICollectionViewFlowLayout alloc] init];
            [layout setScrollDirection:UICollectionViewScrollDirectionVertical];
            [layout setItemSize:CGSizeMake(150, 50)];
            
            
            FbFriendsViewControllerFboard *automaton=[[FbFriendsViewControllerFboard alloc] initWithCollectionViewLayout:layout];
            automaton.title=@"Friend List";
            
            ScheduledPostControllerFboard *schedule=[[ScheduledPostControllerFboard alloc] init];
            schedule.title=@"Scheduled";
            
           //
            [[NSNotificationCenter defaultCenter]removeObserver:self];
            CustomMenuViewControllerFboard *customMenu=[[CustomMenuViewControllerFboard alloc] init];
           customMenu.viewControllers = @[follow,unfollow,profile,post,grp,automaton,schedule,profileView,Search];
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
-(void)callExtendedTokenService
{
    NSError * error=nil;
    NSURLResponse * urlResponse=nil;
    NSString * urlString=[NSString stringWithFormat:@"https://graph.facebook.com/oauth/access_token?client_id=%@&client_secret=%@&grant_type=fb_exchange_token&fb_exchange_token=%@",@"922478037794381",@"512cd1b16c0fd41fac83912333d34bc4",[SingletonClass sharedState].accessTokenString];
    urlString=[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL * url=[NSURL URLWithString:urlString];
    
    NSMutableURLRequest * getRequest=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    [getRequest setHTTPMethod:@"GET"];
    [getRequest addValue:@"application/x-www-form-urlencoded; charset=utf-8" forHTTPHeaderField:@"Content-Type"];
    NSData * data=[NSURLConnection sendSynchronousRequest:getRequest returningResponse:&urlResponse error:&error];
    if (data==nil)
    {
    }
    NSString* newStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    [self getStringBetweenTwoString:@"=" :@"=" :newStr];
    FBSDKAccessToken * token;
    
    if(!error)
    {
        
    }
    
    NSLog(@" response of feeds %@",newStr);

}
-(NSString*)getStringBetweenTwoString:(NSString*)startString :(NSString*)endString :(NSString*)orignalString
{
    NSString *string=orignalString;
    NSRange searchFromRange = [string rangeOfString:startString];
    NSRange searchToRange = [string rangeOfString:endString];
    NSString *substring = [string substringWithRange:NSMakeRange(searchFromRange.location+searchFromRange.length, searchToRange.location-searchFromRange.location-searchFromRange.length)];
    NSLog(@"subs=%@",substring); //subs= are
    return substring;
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
