
//
//  CustomMenuViewController.m
//  MOVYT
//
//  Created by Sumit Ghosh on 27/05/14.
//  Copyright (c) 2014 Sumit Ghosh. All rights reserved.
//

#import "CustomMenuViewControllerFboard.h"
#import "SingletonClass.h"
#import "UIButton+WebCache.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import <QuartzCore/QuartzCore.h>
#import "SUCacheFboard.h"
#import "SUProfileTableViewCellFboard.h"
#import "ProfileTableViewCellFboard.h"
#import "UIImageView+WebCache.h"
#import "AccountViewControllerFboard.h"

@interface CustomMenuViewControllerFboard ()
{
    NSInteger updateValue;
    NSIndexPath *_currentIndexPath;
    UIImageView * bannerImage;
}
@property (nonatomic,strong)UITabBar *customTabBar;
@end

@implementation CustomMenuViewControllerFboard
@synthesize viewControllers = _viewControllers;




#pragma mark -
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
- (BOOL)prefersStatusBarHidden {
    return YES;
}
-(void)viewDidDisappear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter]removeObserver:self];
}

-(void)viewDidAppear:(BOOL)animated
{
       [self.menuTableView reloadData];
}


//Received Notification Method
-(void) reloadMenuTable:(NSNotification *)notify{
    
    id name = [notify object];
    if ([name isKindOfClass:[NSString class]]) {
        
        if ([name isEqualToString:@"LoggedInWithBroadCast"]){
            self.isSignIn = YES;
            
        }
        else if ([name isEqualToString:@"LoggedIn"]){
            self.isSignIn = YES;
        }
        [self.menuTableView reloadData];
    }
}

#pragma mark -
-(void) setViewControllers:(NSArray *)viewControllers{
    
    _viewControllers = [viewControllers copy];
    
    for (UIViewController *viewController in _viewControllers ) {
        [self addChildViewController:viewController];
        
        viewController.view.frame = CGRectMake(0, 90,[UIScreen mainScreen].bounds.size.width , [UIScreen mainScreen].bounds.size.height-140);
        [viewController didMoveToParentViewController:self];
    }
}
-(void) setSecondSectionViewControllers:(NSArray *)secondSectionViewControllers{
    
    _secondSectionViewControllers = [secondSectionViewControllers copy];
    
    for (UIViewController *viewController in _secondSectionViewControllers ) {
        [self addChildViewController:viewController];
        
        viewController.view.frame = CGRectMake(0, 90,[UIScreen mainScreen].bounds.size.width , [UIScreen mainScreen].bounds.size.height-90);
        [viewController didMoveToParentViewController:self];
    }
}
-(void) setSelectedViewController:(UIViewController *)selectedViewController{
    _selectedViewController = selectedViewController;
}

-(void) setSelectedIndex:(NSInteger)selectedIndex{
    _selectedIndex = selectedIndex;
}

-(NSArray *) getAllViewControllers{
    return self.viewControllers;
}
-(void) setSelectedSection:(NSInteger)selectedSection{
    _selectedSection = selectedSection;
}
#pragma mark -
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(_accessTokenChangedNot:)
                                                 name:FBSDKAccessTokenDidChangeNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(_currentProfileChangedNot:)
                                                 name:FBSDKProfileDidChangeNotification
                                               object:nil];
        // It's generally important to check [FBSDKAccessToken currentAccessToken] at
        //  viewDidLoad to see if there is a token cached by the SDK or, resuming
        //  a login flow after eviction.
        // In this app, we want to see if there's a match with the local cache, and if
        //  not, clear the "current" user and token because that indicates either the
        //  SUCache version is incompatible.
    
//     NSInteger account= [[NSUserDefaults standardUserDefaults]integerForKey:@"LoggedInUser"];
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
 
    
   
//    self.tableDatalist=[[NSMutableArray alloc] init];
    
//    if (account>1) {
//        for ( int i=1;i<=account;i++) {
//            [self.tableDatalist addObject:@"Heloo"];
//        }
//  }
  
    
    
    screenSize = [UIScreen mainScreen].bounds;
    self.view.backgroundColor = [UIColor whiteColor];
    
    userDefault = [NSUserDefaults standardUserDefaults];
    
    // Do any additional setup after loading the view.
    //self.view.backgroundColor = [UIColor redColor];
    
    self.screen_height = [UIScreen mainScreen].bounds.size.height;
    self.isSignIn = NO;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadMenuTable:) name:@"UpdateMenuTable" object:nil];
//    self.view.backgroundColor=[UIColor whiteColor];
    //Add View SubView;
    self.mainsubView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, self.screen_height)];
    NSLog(@"Main sub view frame X=-=- %f \n Y == %f",[UIScreen mainScreen].bounds.origin.x,[UIScreen mainScreen].bounds.origin.y);
    self.mainsubView.backgroundColor = [UIColor whiteColor];
    self.mainsubView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.mainsubView.layer.shadowOpacity = 0.4f;
    self.mainsubView.layer.shadowOffset = CGSizeMake(0.0f, 15.0f);
    self.mainsubView.layer.shadowRadius = 10.0f;
    self.mainsubView.layer.masksToBounds = NO;
    [self.view addSubview:self.mainsubView];
    
    //Add Header View
    CGFloat hh;
    CGRect frame_b;
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        hh = 75;
        frame_b = CGRectMake(680, 30,30,21);
        
    }
    else{
        hh = 55;
        
        frame_b = CGRectMake(20, 20,30,21);
//        if(IS_IPHONE_6)
//        {
//            frame_b = CGRectMake(310, 20, 45, 25);
//        }
//        else if (IS_IPHONE_6P)
//        {
//            frame_b = CGRectMake(349, 20, 45, 25);
//
//        }
    }
    CGRect frame = CGRectMake(0, 0, screenSize.size.width, hh);
    
    self.headerView = [[UIView alloc] initWithFrame:frame];
     self.headerView.backgroundColor =ThemeColorFboard;
    [self.mainsubView addSubview:self.headerView];
    
    NSLog(@"Width menu== %f",screenSize.size.width);
    
    
    //=======================================
    // Add Container View
    frame = CGRectMake(0,55, screenSize.size.width, screenSize.size.height);
    self.contentContainerView = [[UIView alloc] initWithFrame:frame];
    self.contentContainerView.backgroundColor = [UIColor grayColor];
    self.contentContainerView.autoresizingMask = UIViewAutoresizingFlexibleHeight;
    [self.mainsubView addSubview:self.contentContainerView];
    //------------------
    
    self.menuButton = [UIButton buttonWithType:UIButtonTypeCustom];
    self.menuButton.frame = frame_b;
    self.menuButton.titleLabel.font = [UIFont systemFontOfSize:9.0f];
    self.menuButton.titleLabel.shadowOffset = CGSizeMake(0.0f, 0.0f);
    
    //self.menuButton.titleLabel.layer.
    [self.menuButton addTarget:self action:@selector(menuButtonClciked) forControlEvents:UIControlEventTouchUpInside];
    [self.menuButton setBackgroundImage:[UIImage imageNamed:@"menu.png"] forState:UIControlStateNormal];
    
    [self.headerView addSubview:self.menuButton];
    
    self.leftProfileButton
 = [UIButton buttonWithType:UIButtonTypeCustom];
    self.leftProfileButton
.frame = CGRectMake(self.view.frame.size.width-60, 15, 30, 30);
    self.leftProfileButton
.titleLabel.font = [UIFont systemFontOfSize:9.0f];
    self.leftProfileButton
.titleLabel.shadowOffset = CGSizeMake(0.0f, 0.0f);
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
        //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:index.section+index.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
    //NSString *profileID=[FBSDKAccessToken currentAccessToken].userID;
        //self.menuButton.titleLabel.layer.
    [self.leftProfileButton
 addTarget:self action:@selector(leftProfileButtonClicked) forControlEvents:UIControlEventTouchUpInside];
    [self.leftProfileButton setImage:[UIImage imageNamed:@"accountOpenBtn.png"] forState:UIControlStateNormal];
    
    [self.headerView addSubview:self.leftProfileButton
];

    
    
    //===================================
    
    //Add Menu Lable
    self.menuLabel = [[UILabel alloc] initWithFrame:CGRectMake(110, 15, 100, 30)];
    self.menuLabel.backgroundColor = [UIColor clearColor];
    self.menuLabel.font = [UIFont boldSystemFontOfSize:15];
    self.menuLabel.textColor = [UIColor whiteColor];
    self.menuLabel.textAlignment = NSTextAlignmentCenter;
    self.menuLabel.text = _selectedViewController.title;
    [self.headerView addSubview:self.menuLabel];
    
    //====================================
    
    self.selectedIndex = 0;
    self.selectedViewController = [_viewControllers objectAtIndex:0];
    [self updateViewContainer];
    [self createMenuTableView];
    [self createAccountTable];
    //Adding Swipr Gesture
    
          //===============
    self.swipeGestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeGesture:)];
   self.swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.mainsubView addGestureRecognizer:self.swipeGestureRight];
}

#pragma mark -
#pragma mark -Create Table

- (void)_accessTokenChangedNot:(NSNotification *)notification
{
    FBSDKAccessToken *token = notification.userInfo[FBSDKAccessTokenChangeNewKey];
    
    if (!token) {
        [self _deselectRow];
    } else {
        ProfileTableViewCellFboard *cell = (ProfileTableViewCellFboard *)[self.accountTableView cellForRowAtIndexPath:_currentIndexPath];
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        NSInteger slot = [self _userSlotFromIndexPath:_currentIndexPath];
        SUCacheItem *item = [SUCacheFboard itemForSlot:slot] ?: [[SUCacheItem alloc] init];
        if (![item.token isEqualToAccessToken:token]) {
            item.token = token;
            [SUCacheFboard saveItem:item slot:slot];
            cell.userID = token.userID;
        }
    }
}

- (void)_currentProfileChangedNot:(NSNotification *)notification
{
    NSInteger slot = [self _userSlotFromIndexPath:_currentIndexPath];
    
    FBSDKProfile *profile = notification.userInfo[FBSDKProfileChangeNewKey];
    if (profile) {
        SUCacheItem *cacheItem = [SUCacheFboard itemForSlot:slot];
        cacheItem.profile = profile;
        [SUCacheFboard saveItem:cacheItem slot:slot];
        
        ProfileTableViewCellFboard *cell = (ProfileTableViewCellFboard *)[self.accountTableView cellForRowAtIndexPath:_currentIndexPath];
        cell.userName = cacheItem.profile.name;
    }
}



-(void)createAccountTable
{
    if (!self.accountTableView)
    {
        self.selectedIndex = 0;
        self.accountTableView = [[UITableView alloc] initWithFrame:CGRectMake(100, 0,screenSize.size.width-100,screenSize.size.height-50) style:UITableViewStylePlain];
        
        self.accountTableView.backgroundColor =  [UIColor whiteColor];
        
        self.accountTableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
        self.accountTableView.delegate = self;
        self.accountTableView.dataSource = self;
        self.accountTableView.layer.borderColor=(__bridge CGColorRef)([UIColor blackColor]);
        self.accountTableView.layer.borderWidth=6.0;
        
        
//        [ self.accountTableView registerClass:[SUProfileTableViewCell class] forCellReuseIdentifier:@"SUProfileTableViewCel"];
        UIButton *createAccount = [UIButton buttonWithType:UIButtonTypeCustom];
        createAccount.frame = CGRectMake(0, 5, self.accountTableView.frame.size.width, 40);
        createAccount.titleLabel.font = [UIFont systemFontOfSize:12.0f];
        createAccount.backgroundColor=[UIColor clearColor];
          [createAccount setTitle:@"  Add More Account"  forState:UIControlStateNormal];
        createAccount.titleLabel.textColor=[UIColor blackColor];
        [createAccount setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [createAccount setImage:[UIImage imageNamed:@"inner_follow.png"] forState:UIControlStateNormal];
//        createAccount.layer.borderColor=(__bridge CGColorRef)([UIColor whiteColor ]);
//        createAccount.layer.borderWidth=2.0;
//            //self.menuButton.titleLabel.layer.
        [createAccount addTarget:self action:@selector(createNewAccount) forControlEvents:UIControlEventTouchUpInside];
      
        self.accountTableView.tableFooterView=createAccount;
        self.accountTableView.tableFooterView.userInteractionEnabled=YES;
        NSInteger * itemNo=(NSInteger)0;
        SUCacheItem *item = [SUCacheFboard itemForSlot:itemNo];

        bannerImage=[[UIImageView alloc]init];
        bannerImage.frame=CGRectMake(0, 0,screenSize.size.width-100,150);
        bannerImage.image=[UIImage imageNamed:@""];
       // bannerImage.contentMode = UIViewContentModeScaleAspectFit;
        [self getBannertUrl:item.token.userID bannerImageView:bannerImage];
        self.accountTableView.tableHeaderView=bannerImage;
      //----------
        profilePicCurrentUser=[[UIImageView alloc]init];
        profilePicCurrentUser.frame=CGRectMake(0,120,30,30);
        [self setProfilePicInAccountHeader:item.token.userID profileImageView:profilePicCurrentUser];
        profilePicCurrentUser.clipsToBounds=YES;
        [bannerImage addSubview:profilePicCurrentUser];
    }
    else
    {
        [self.accountTableView reloadData];
    }
    
  [self.view insertSubview:self.accountTableView belowSubview:self.mainsubView];
    
    
    
    
}
-(void) createMenuTableView
{
    
    if (!self.menuTableView)
    {
        self.selectedIndex = 0;
        self.menuTableView = [[UITableView alloc] initWithFrame:CGRectMake(0,55,screenSize.size.width-100, self.screen_height-80) style:UITableViewStylePlain];
        
//        self.menuTableView.backgroundColor =  [UIColor colorWithRed:(CGFloat)39/255 green:(CGFloat)39/255 blue:(CGFloat)41/255 alpha:1];
        self.menuTableView.backgroundColor =  [UIColor whiteColor];
        self.menuTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        self.menuTableView.delegate = self;
        self.menuTableView.dataSource = self;
        self.menuTableView.backgroundColor=[UIColor clearColor];
        
        

    }
    else
    {
        [self.menuTableView reloadData];
    }
    
    [self.view insertSubview:self.menuTableView belowSubview:self.mainsubView];
    UIView * viewLogoBg=[[UIView alloc]init];
    viewLogoBg.frame=CGRectMake(0, 0, screenSize.size.width-100,55);
    viewLogoBg.backgroundColor=[UIColor whiteColor];
    [self.view insertSubview:viewLogoBg belowSubview:self.mainsubView];
    
    
    UIImageView * viewLogo=[[UIImageView alloc]init];
    viewLogo.frame=CGRectMake(30, 15, 150,24);
   viewLogo.image=[UIImage imageNamed:@"fbrandpro.png"];
    [viewLogoBg insertSubview:viewLogo belowSubview:self.mainsubView];
    if (!lblUserName)
    {
        lblUserName = [[UILabel alloc] initWithFrame:CGRectMake(self.view.frame.size.width-100,50, 50, 20)];
        lblUserName.font=[UIFont boldSystemFontOfSize:12];
        lblUserName.textColor=[UIColor whiteColor];
        //        [self.headerView addSubview:lblUserName];
        [self.view insertSubview:lblUserName belowSubview:self.mainsubView];
    }
    if (!lblUserRank) {
        lblUserRank=[[UILabel alloc]initWithFrame:CGRectMake(self.view.frame.size.width-100, 70,90, 20)];
        lblUserRank.font=[UIFont boldSystemFontOfSize:12];
        lblUserRank.textColor=[UIColor whiteColor];
        [self.view insertSubview:lblUserRank belowSubview:self.mainsubView];
    }
    
    
}

-(void)createNewAccount
{
      [self _deselectRow];
      NSInteger val=[[NSUserDefaults standardUserDefaults] integerForKey:@"LoggedInUser"];

   _currentIndexPath = [NSIndexPath indexPathForRow:val inSection:0];
//    _currentIndexPath =[NSIndexPath indexPathWithIndex:self.tableDatalist.count-1];

  NSInteger slot = [self _userSlotFromIndexPath:_currentIndexPath];
    NSLog(@"integer slot %ld",(long)slot);
//    NSArray *permissions =  [[NSArray alloc] initWithObjects:@"publish_actions", @"user_likes",
// nil];
    
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
        
        NSLog(@"result grand permission - %@  declinedPermissions - %@",result.grantedPermissions,result.declinedPermissions);
        if (error || result.isCancelled)
        {
//            cell.userName = @"Empty slot";
            [self _deselectRow];
        }else{
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.accountTableView beginUpdates];
              
//
              
                    [[NSUserDefaults standardUserDefaults]setInteger:val+1 forKey:@"LoggedInUser"];
                    [[NSUserDefaults standardUserDefaults]synchronize];

                    
                    [self.accountTableView insertRowsAtIndexPaths:@[_currentIndexPath]
                                                 withRowAnimation:UITableViewRowAnimationRight];

                
                [self.accountTableView endUpdates];
//        }
            });
        }

    }];

    
}

//- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
//    if (tableView==self.accountTableView) {
//        return 50.0f;
//    }
//    else{
//        return 10;
//    }
//    
//}
- (void)_deselectRow
{
//    [self.accountTableView cellForRowAtIndexPath:_currentIndexPath].accessoryType = UITableViewCellAccessoryNone;
//    _currentIndexPath = nil;
    [FBSDKAccessToken setCurrentAccessToken:nil];
    [FBSDKProfile setCurrentProfile:nil];
}

#pragma mark -

-(void)handleSwipeGesture:(UISwipeGestureRecognizer *)swipeGesture
{
//    if (swipeGesture.direction==UISwipeGestureRecognizerDirectionLeft) {
    
   if (self.mainsubView.frame.origin.x!=0) {
        
                        [UIView animateWithDuration:.5 animations:^{
                           self.mainsubView.frame = CGRectMake(0, 0,screenSize.size.width, screenSize.size.height);
                            
                        }completion:^(BOOL finish){
                            self.swipeGestureRight.direction = UISwipeGestureRecognizerDirectionLeft;
                    }];
   }else{
   
   }
    

}
                         
#pragma mark -
-(void) menuButtonClciked
{
    self.accountTableView.hidden=YES;
    self.menuTableView.hidden=NO;
    if (self.mainsubView.frame.origin.x>=120) {
        
        [UIView animateWithDuration:.5 animations:^{
            self.mainsubView.frame = CGRectMake(0, 0,screenSize.size.width, screenSize.size.height);
            
        }completion:^(BOOL finish){
            
           self.swipeGestureRight.direction = UISwipeGestureRecognizerDirectionLeft;
        }];
    }
    else{
        [UIView animateWithDuration:.5 animations:^{
            self.mainsubView.frame = CGRectMake(screenSize.size.width-100, 0,screenSize.size.width, screenSize.size.height);
            
        }completion:^(BOOL finish){
           self.swipeGestureRight.direction = UISwipeGestureRecognizerDirectionLeft;
        }];
    }
}

-(void)leftProfileButtonClicked{

    self.accountTableView.hidden=NO;
    self.menuTableView.hidden=YES;
    if (self.mainsubView.frame.origin.x<0) {
        
        [UIView animateWithDuration:.5 animations:^{
            self.mainsubView.frame = CGRectMake(0, 0,screenSize.size.width, screenSize.size.height);
            
        }completion:^(BOOL finish){
            
           self.swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
        }];
    }
    else{
        [UIView animateWithDuration:.5 animations:^{
            self.mainsubView.frame = CGRectMake(100-screenSize.size.width, 0,screenSize.size.width, screenSize.size.height);
            
        }completion:^(BOOL finish){
            self.swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
        }];
    }

    
}

#pragma mark -
#pragma mark TableView Delegate and DataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (tableView==self.accountTableView)
    {
        return 1;
    }
    else
        return 1;
}


- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
           return nil;
   
}

-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(tableView==self.menuTableView)
    {
    if (section==0) {
        
        return self.viewControllers.count;
        
    }
    }
    else if (tableView==self.accountTableView)
    {
//        if (section==0) {
//            return 1;
//        }else{
            NSInteger account= [[NSUserDefaults standardUserDefaults]integerForKey:@"LoggedInUser"];
            return account;
//        }
    }
    return 0;
}

-(void) tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    UIColor *firstColor =  [UIColor colorWithRed:(CGFloat)39/255 green:(CGFloat)39/255 blue:(CGFloat)41/255 alpha:1];
    UIColor *secColor = [UIColor colorWithRed:(CGFloat)48/255 green:(CGFloat)48/255 blue:(CGFloat)50/255 alpha:1];
    CAGradientLayer *layer = [CAGradientLayer layer];
    layer.frame = cell.contentView.frame;
    layer.colors = [NSArray arrayWithObjects:(id)firstColor.CGColor,(id)secColor.CGColor, nil];
//    cell.accessoryView.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"tik.png"]];
    if (tableView==self.menuTableView) {
//         [cell.contentView.layer insertSublayer:layer atIndex:0];
        cell.textLabel.textColor = [UIColor blackColor];
        cell.textLabel.font = [UIFont boldSystemFontOfSize:14.0f];
    }else{
        cell.textLabel.textColor = [UIColor blackColor];
        cell.textLabel.font = [UIFont boldSystemFontOfSize:14.0f];
    }
   
    
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (tableView==self.menuTableView) {
        return 2;
    }else
    {
       
            return 0;
    
    }
    
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return 3.0;
}
-(UITableViewCell *) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
        //Check Section
    if(tableView==self.menuTableView)
    {
        
        static NSString *CellIdentifier = @"Cell Identifier";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }

        
    NSString * title=[NSString stringWithFormat:@"%@",[(UIViewController *)[_viewControllers objectAtIndex:indexPath.row] title]];
    NSLog(@"Title = %@",title);
        UIImageView *iconImage=[[UIImageView alloc]init];
        iconImage.frame=CGRectMake(10, 10,20,20);
        [cell.contentView addSubview:iconImage];
        UILabel * titleLbl=[[UILabel alloc]init];
        titleLbl.frame = CGRectMake(50,10, SCREEN_WIDTH-90, 20);
        titleLbl.text = title;
        [cell.contentView addSubview:titleLbl];
        if ([title isEqualToString:@"Home Feeds"]) {
             iconImage.image=[UIImage imageNamed:@"home_feeds.png"];
        }else if([title isEqualToString:@"Pages"]){
        iconImage.image=[UIImage imageNamed:@"pages.png"];
        
        }else if([title isEqualToString:@"Post schedule"]){
            iconImage.image=[UIImage imageNamed:@"post_schedule.png"];
            
        }else if([title isEqualToString:@"My Groups"])
        {
          iconImage.image=[UIImage imageNamed:@"my_groups.png"];
        }
        else if([title isEqualToString:@"My Feeds"])
        {
            iconImage.image=[UIImage imageNamed:@"my_feeds.png"];
        }
        else if([title isEqualToString:@"Scheduled"])
        {
            iconImage.image=[UIImage imageNamed:@"scheduled.png"];
        }
        else if([title isEqualToString:@"Profile"]){
            iconImage.image=[UIImage imageNamed:@"profile.png"];
        }
        
        else if([title isEqualToString:@"Friend List"]){
            iconImage.image=[UIImage imageNamed:@"friend_list.png"];
        }
        else
            if([title isEqualToString:@"Search"])
            {
                iconImage.image=[UIImage imageNamed:@"search.png"];
            }
       [cell.textLabel setFont:[UIFont fontWithName:@"Zapfino" size:3.0]];
        cell.textLabel.font=[UIFont fontWithName:@"Zapfino" size:3.0];
        return cell;
    }
    else
    {

        static NSString *CellIdentifier = @"Cell Identifier";
        
        ProfileTableViewCellFboard *cell2 = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
        cell2.backgroundColor=[UIColor clearColor];
        
        if (cell2 == nil) {
            cell2 = [[ProfileTableViewCellFboard alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
            cell2.selectionStyle = UITableViewCellSelectionStyleNone;
        }
//        NSInteger account= [[NSUserDefaults standardUserDefaults]integerForKey:@"LoggedInUser"];
        
        NSInteger slot = [self _userSlotFromIndexPath:indexPath];
              SUCacheItem *item = [SUCacheFboard itemForSlot:slot];

        cell2.textLabel.textColor = [UIColor blackColor];
        cell2.textLabel.font = [UIFont boldSystemFontOfSize:14.0f];
        cell2.contentView.backgroundColor=[UIColor whiteColor];
        
        cell2.backgroundColor=[UIColor whiteColor];
        cell2.userName = item.profile.name ?: @"Add New User";
        cell2.userID = item.token.userID;
        NSLog(@"username %@,Userid %@ slot %ld",item.profile.name,item.token.userID,(long)slot);
    
        if ([item.token isEqualToAccessToken:[FBSDKAccessToken currentAccessToken]]) {
            _currentIndexPath = indexPath;
            cell2.accessoryType = UITableViewCellAccessoryCheckmark;
        }
        return cell2;


    }
    
    
}


//- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
//    return ([SUCache itemForSlot:[self _userSlotFromIndexPath:indexPath]] != nil);
//}
//

- (NSInteger)_userSlotFromIndexPath:(NSIndexPath *)indexPath
{
        // Since section 0 has 1 row, we can use this cheap trick
        // so that the "Primary User" cell is slot 0 and the rest
        // follow.
    return indexPath.row + indexPath.section;
}

- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath {
    return @"Remove";
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    return ([SUCacheFboard itemForSlot:[self _userSlotFromIndexPath:indexPath]] != nil);
}

- (void)tableView:(UITableView *)tableView
commitEditingStyle:(UITableViewCellEditingStyle)editingStyle
forRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSInteger val=[[NSUserDefaults standardUserDefaults] integerForKey:@"LoggedInUser"];

    if (editingStyle == UITableViewCellEditingStyleDelete) {
        NSInteger slot = [self _userSlotFromIndexPath:indexPath];
        [SUCacheFboard deleteItemInSlot:slot];

        
        ProfileTableViewCellFboard *cell = (ProfileTableViewCellFboard *)[tableView cellForRowAtIndexPath:indexPath];
        cell.userName = @"Add New User";
        cell.userID = nil;
        cell.contentView.backgroundColor=[UIColor whiteColor];
        cell.textLabel.textColor = [UIColor blackColor];
        cell.textLabel.font = [UIFont boldSystemFontOfSize:14.0f];
        cell.accessoryType = UITableViewCellAccessoryNone;
        if ([_currentIndexPath compare:indexPath] == NSOrderedSame) {
            [self _deselectRow];
        }
        
        [[NSUserDefaults standardUserDefaults]setInteger:val-1 forKey:@"LoggedInUser"];
        [[NSUserDefaults standardUserDefaults]synchronize];
        
        if (val-1==0) {
            
            [[NSNotificationCenter defaultCenter]removeObserver:self name:FBSDKAccessTokenDidChangeNotification object:nil];
            [[NSNotificationCenter defaultCenter]removeObserver:self name:FBSDKProfileDidChangeNotification object:nil];
            
            
            self.account=[[AccountViewControllerFboard alloc] init];
            [self.navigationController pushViewController:self.account animated:YES];
        }


        for (NSInteger i=indexPath.row+1; i<val; i++) {
            SUCacheItem *cacheItem1 = [SUCacheFboard itemForSlot:i];

            ProfileTableViewCellFboard *cell = (ProfileTableViewCellFboard*)[tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:i-1 inSection:0]];
            cell.userName =cacheItem1.profile.name;
            cell.userID = cacheItem1.token.userID;

            [SUCacheFboard saveItem:cacheItem1 slot:i-1];

            if (i==val-1) {
                [tableView deleteRowsAtIndexPaths:@[[NSIndexPath indexPathForRow:i-1 inSection:0]] withRowAnimation:UITableViewRowAnimationLeft];
            }

        }
      
        
        

   [tableView reloadData];
        
           }
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if(tableView==self.menuTableView)
    {
    //Dismiss Menu TableView with Animation
    [UIView animateWithDuration:.5 animations:^{
        
        self.mainsubView.frame = CGRectMake(0, 0, screenSize.size.width, screenSize.size.height);
        
    }completion:^(BOOL finished){
        //After completion
        //first check if new selected view controller is equals to previously selected view controller
        UIViewController *newViewController = [_viewControllers objectAtIndex:indexPath.row];
        if ([newViewController isKindOfClass:[UINavigationController class]]) {
            [(UINavigationController *)newViewController popToRootViewControllerAnimated:YES];
        }
        if (self.selectedIndex==indexPath.row  && self.selectedSection == indexPath.section) {
          //  return;
        }
        if (indexPath.row==0) {
            self.customTabBar.selectedItem = [self.customTabBar.items objectAtIndex:0];
        }
        else{
            self.customTabBar.selectedItem = nil;
        }
        
        _selectedSection = indexPath.section;
        _selectedIndex = indexPath.row;
        
        [self getSelectedViewControllers:newViewController];
        updateValue = 0;
    }];
    self.topicButton.hidden=YES;
    }else
    {
        _currentIndexPath=indexPath;
        FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPath.row+indexPath.section].token;
        if (token) {

        [SingletonClass sharedState].selectedUserIndex=indexPath;
            [self getBannertUrl:token.userID bannerImageView:bannerImage];
  [self setProfilePicInAccountHeader:token.userID profileImageView:profilePicCurrentUser];
            
      [[NSNotificationCenter defaultCenter]postNotificationName:@"CurrentUserChangedNotification" object:nil];
        if (self.mainsubView.frame.origin.x<0) {
            
            [UIView animateWithDuration:.5 animations:^{
                self.mainsubView.frame = CGRectMake(0, 0,screenSize.size.width, screenSize.size.height);
                
            }completion:^(BOOL finish){
                
                self.swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
            }];
        }
        
        }else{
            [self _deselectRow];
        
            FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
//            if (indexPath.section == 1) {
               login.loginBehavior = FBSDKLoginBehaviorWeb;
//            }
            
       NSArray *permissions =  [[NSArray alloc] initWithObjects:@"publish_actions", nil];
            
//            NSArray *permissions =  [[NSArray alloc] initWithObjects:@"contact_email",
//                                     @"user_birthday",
//                                     @"user_work_history",
//                                     @"user_likes",
//                                     @"user_posts",
//                                     @"user_hometown",
//                                     @"user_photos",
//                                     @"user_about_me",
//                                     @"public_profile",
//                                     @"user_location",
//                                     @"user_friends",
//                                     @"email",
//                                     nil];

            
                //            login.loginBehavior = FBSDKLoginBehaviorWeb;
            
            [login logInWithPublishPermissions:permissions handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
                
                
                NSLog(@"result grand permission - %@  declinedPermissions - %@",result.grantedPermissions,result.declinedPermissions);
                if (error || result.isCancelled) {
    
                    [self _deselectRow];
                }
            }];
            
        }

    }
}
#pragma mark Get BanerImage
-(void)getBannertUrl:(NSString *)userId bannerImageView:(UIImageView*)bannerImageView
{
    //Start an activity indicator here
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        //Call your function or whatever work that needs to be done
        //Code in this part is run on a background thread
        
        NSError * error=nil;
        NSURLResponse * urlResponse=nil;
        NSURL * url=[NSURL  URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@?fields=cover&access_token=%@",userId,[SingletonClass sharedState].accessTokenString]];
        
        NSMutableURLRequest * getRequest=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
        [getRequest setHTTPMethod:@"GET"];
        [getRequest addValue:@"application/x-www-form-urlencoded; charset=utf-8" forHTTPHeaderField:@"Content-Type"];
        NSData * data=[NSURLConnection sendSynchronousRequest:getRequest returningResponse:&urlResponse error:&error];
        if (data==nil)
        {
        }
        id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
        if(!error)
        {
            
        }
        
        NSLog(@" response of feeds %@",response);
        
       NSURL *bannerImageUrl=[NSURL URLWithString:[[response objectForKey:@"cover"] objectForKey:@"source"]];
        
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            
            //Stop your activity indicator or anything else with the GUI
            //Code here is run on the main thread
            [bannerImageView setImageWithURL:bannerImageUrl placeholderImage:[UIImage imageNamed:@"defaultBanner.png"]];
            
        });
    });
}

-(void)setProfilePicInAccountHeader:(NSString *)userId profileImageView:(UIImageView*)profileImageView
{
    //Start an activity indicator here
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        //Call your function or whatever work that needs to be done
        //Code in this part is run on a background thread
        
        NSError * error=nil;
        NSURLResponse * urlResponse=nil;
        NSURL * url=[NSURL  URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@?fields=picture&access_token=%@",userId,[SingletonClass sharedState].accessTokenString]];
        
        NSMutableURLRequest * getRequest=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
        [getRequest setHTTPMethod:@"GET"];
        [getRequest addValue:@"application/x-www-form-urlencoded; charset=utf-8" forHTTPHeaderField:@"Content-Type"];
        NSData * data=[NSURLConnection sendSynchronousRequest:getRequest returningResponse:&urlResponse error:&error];
        if (data==nil)
        {
        }
        id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
        if(!error)
        {
            
        }
        
        NSLog(@" response of feeds %@",response);
        
        NSURL *bannerImageUrl=[NSURL URLWithString:[[[response objectForKey:@"picture"] objectForKey:@"data"] objectForKey:@"url"]];
        
        dispatch_async(dispatch_get_main_queue(), ^(void) {
            
            //Stop your activity indicator or anything else with the GUI
            //Code here is run on the main thread
            [profileImageView setImageWithURL:bannerImageUrl placeholderImage:[UIImage imageNamed:@""]];
            
        });
    });
   

}

#pragma mark -
-(void) getSelectedViewControllers:(UIViewController *)newViewController{
    // selected new view controller
    UIViewController *oldViewController = _selectedViewController;
    
    if (newViewController != nil) {
        [oldViewController.view removeFromSuperview];
        _selectedViewController = newViewController;
        
        //Update Container View with selected view controller view
        [self updateViewContainer];
        //Check Delegate assign or not
    }
}
-(void) updateViewContainer
{
    self.selectedViewController.view.autoresizingMask = UIViewAutoresizingFlexibleHeight;
    
    self.selectedViewController.view.frame = self.contentContainerView.bounds;
    self.menuLabel.text=self.selectedViewController.title;
    NSLog(@"menu label -=- %@",self.menuLabel.text);
    
    [self.contentContainerView addSubview:self.selectedViewController.view];
    
}
#pragma mark-----


//-(void)dealloc
//{
//    NSLog(@"Dealloc Called");
//    [[NSNotificationCenter defaultCenter]removeObserver:self];
//}
@end
