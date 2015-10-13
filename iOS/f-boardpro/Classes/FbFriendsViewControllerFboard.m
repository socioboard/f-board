//
//  FBAutomationViewController.m
//  SwitchUserSample
//
//  Created by Socioboard 1 on 4/29/15.
//
//

#import "FbFriendsViewControllerFboard.h"
#import "AppDelegateFboard.h"
#import "MBProgressHUD.h"
#import "ProfileViewControllerFboard.h"
#import "SUCacheFboard.h"
#import "UIImageView+WebCache.h"
#import "ImageViewCustomCellFboard.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import "SingletonClass.h"

@interface FbFriendsViewControllerFboard ()
{
    UITableView *friendstableView;
}

@end

@implementation FbFriendsViewControllerFboard

static NSString * const reuseIdentifier = @"Cell";

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor=[UIColor whiteColor];
    self.refreshControl = [[UIRefreshControl alloc] init];
    [self.refreshControl addTarget:self action:@selector(refershControlAction) forControlEvents:UIControlEventValueChanged];
    [self.collectionView addSubview:self.refreshControl];
   
    NSArray *itemArray=[NSArray arrayWithObjects:@"Fboard Friends",@"All Friends", nil];
    self.segmentControl = [[UISegmentedControl alloc] initWithItems:itemArray];
    self.segmentControl.frame =CGRectMake(0,5,self.view.frame.size.width,30);
    [self.segmentControl addTarget:self action:@selector(mySegmentControlAction:) forControlEvents: UIControlEventValueChanged];
    [self.segmentControl setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor blackColor]} forState:UIControlStateNormal];
    [self.segmentControl setBackgroundColor:[UIColor whiteColor]];
    [self.segmentControl setTintColor:ThemeColorFboard];
    self.segmentControl.selectedSegmentIndex=0;
    [self.view addSubview:self.segmentControl];

    // Uncomment the  line to preserve selection between presentations
    // self.clearsSelectionOnViewWillAppear = NO;
//    UICollectionViewFlowLayout *layout=[[UICollectionViewFlowLayout alloc] init];
//    [layout setScrollDirection:UICollectionViewScrollDirectionVertical];
//    [layout setItemSize:CGSizeMake(125, 40)];
//    
//    [self.collectionView setCollectionViewLayout:layout];
    // Register cell classes
    self.collectionView.frame= CGRectMake(0, 50, self.view.frame.size.width, self.view.frame.size.height-50);
   // self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
   // self.collectionView.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
    self.collectionView.backgroundColor=[UIColor whiteColor];
    [self.collectionView registerClass:[ImageViewCustomCellFboard class] forCellWithReuseIdentifier:reuseIdentifier];
    [self createfriendstable];
    // Do any additional setup after loading the view.
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
   
    self.navigationController.navigationBarHidden=YES;
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(FetchFreinds) name:@"CurrentUserChangedNotification" object:nil];
    
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    self.mutOldArray=[[NSMutableArray alloc] init];
    self.mutNowArray=[[NSMutableArray alloc] init];
    [self FetchFreinds];
    [self fetchAllFriends];
}
-(void)createfriendstable
{
    friendstableView = [[UITableView alloc]init];
    friendstableView.frame = CGRectMake(0,55, SCREEN_WIDTH, SCREEN_HEIGHT-55);
    friendstableView.delegate=self;
    friendstableView.dataSource=self;
    friendstableView.separatorStyle=UITableViewCellSeparatorStyleNone;
    [self.view addSubview:friendstableView];
    //-----------
    UIView * footerView=[[UIView alloc]init];
    footerView.frame=CGRectMake(0, 0, SCREEN_WIDTH, 80);
    friendstableView.tableFooterView=footerView;
    
}
#pragma mark Table Delegates
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 50;
}
-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
   return 1;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.segmentControl.selectedSegmentIndex==0) {
        return self.mutNowArray.count;
    }else{
        return self.allFriendsArray.count;
    }
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"friendscell";
    
   UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
    
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.contentView.layer.shadowOpacity=.5;
    }
    //-----------
    
    cell.contentView.backgroundColor = [UIColor clearColor];
    
    
    

    //----
    
    
    
    NSDictionary *dic;
    NSString *strUrl;
    if (self.segmentControl.selectedSegmentIndex==0)
    {
        dic=[self.mutNowArray objectAtIndex:indexPath.row];
        strUrl=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",[dic objectForKey:@"id"]];
    }
    else
    {
        dic=[self.allFriendsArray objectAtIndex:indexPath.row];
        strUrl=[[[dic objectForKey:@"picture"]objectForKey:@"data"]objectForKey:@"url"];
    }
    //     NSDictionary *dic=[self.mutNowArray objectAtIndex:indexPath.row];
    NSURL *url=[NSURL URLWithString:strUrl];
    UIImageView * userImage=[[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 30, 30)];
    [userImage setImageWithURL:url placeholderImage:[UIImage imageNamed:@""]];
    [cell.contentView addSubview:userImage];
    
    NSString* nameStr = [dic objectForKey:@"name"];
   
    UILabel * nameLbl=[[UILabel alloc]init];
    nameLbl.frame=CGRectMake(50,5,SCREEN_WIDTH-100,40);
    nameLbl.font=[UIFont systemFontOfSize:15];
    nameLbl.numberOfLines=0;
    nameLbl.lineBreakMode=NSLineBreakByWordWrapping;
    nameLbl.text=[NSString stringWithFormat:@"%@",nameStr];
    [cell.contentView addSubview:nameLbl];
    if (self.segmentControl.selectedSegmentIndex==0)
    {
        if(self.mutNowArray.count-1==indexPath.row)
        {
            
        }
    }
    else
    {
       
        if(self.allFriendsArray.count-1==indexPath.row)
        {
            if(paginationUrl)
            [self pagingationInAllFriends:paginationUrl];
        }
    }

    
    return cell;
    
}

-(void)mySegmentControlAction:(UISegmentedControl *)seg
{
        [friendstableView reloadData];
}

-(void)unfriendYou{
    self.unfriendArray=[[NSMutableArray alloc] init];
    self.newfriendArray=[[NSMutableArray alloc] init];
    for (NSDictionary *dic1 in self.mutOldArray) {
        int i=0;
         BOOL isItemFound=0;
        for (NSDictionary *dic2 in self.mutNowArray ) {
            i++;
           
            if ( [[dic2 objectForKey:@"id"] isEqualToString:[dic1 objectForKey:@"id"]]) {
                isItemFound=1;
            }
            
        }
        if (isItemFound==0) {
    
            [self.unfriendArray addObject:dic1];
        }

    }
        for (NSDictionary *dic1 in self.mutNowArray) {
            int i=0;BOOL isItemFound=0;
            for (NSDictionary *dic2 in self.mutOldArray ) {
                i++;
                
                if ( [[dic2 objectForKey:@"id"] isEqualToString:[dic1 objectForKey:@"id"]]) {
                    isItemFound=1;
                }
                
                
            }
            if (isItemFound==0) {
                [self.newfriendArray addObject:dic1];
                
            }
        }
    if (self.newfriendArray) {
        [self.mutOldArray addObjectsFromArray:self.newfriendArray];
    }
    
[[NSUserDefaults standardUserDefaults] setObject:self.mutOldArray
                                          forKey:[FBSDKAccessToken currentAccessToken].userID];
}
#pragma mark Fetch Friends

-(void)FetchFreinds{
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    
    if (self.mutOldArray&&self.mutNowArray) {
        self.mutOldArray=[[NSMutableArray alloc] init];
        self.mutNowArray=[[NSMutableArray alloc] init];
    }
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
        //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:index.section+index.row].token;
    if (token) {
        
    
    [FBSDKAccessToken setCurrentAccessToken:token];
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/friends" parameters:nil];
//     FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/invitable_friends?fields=name,picture,id" parameters:nil];
    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
    NSArray *arr=[response objectForKey:@"data"];
       NSArray *friendList= [[NSUserDefaults standardUserDefaults]objectForKey:[FBSDKAccessToken currentAccessToken].userID] ;
        
        [self.mutNowArray addObjectsFromArray:arr];
        if (friendList) {
             [self.mutOldArray addObjectsFromArray:friendList];
            [self unfriendYou];

        }else{
            [self.mutOldArray addObjectsFromArray:arr];
            [[NSUserDefaults standardUserDefaults]setObject:arr forKey:[FBSDKAccessToken currentAccessToken].userID];
        
        }
       
               [self.collectionView reloadData];
        
    }];
    
    }
    
}
-(void)fetchAllFriends
{
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    
       NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
    //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:index.section+index.row].token;
    if (token) {
        
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/taggable_friends" parameters:nil];
       
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id response, NSError *error) {
            NSArray *arr=[response objectForKey:@"data"];
            paginationUrl=[[response objectForKey:@"paging"]objectForKey:@"next"];
            self.allFriendsArray=arr;
            NSArray *friendList= [[NSUserDefaults standardUserDefaults]objectForKey:[FBSDKAccessToken currentAccessToken].userID] ;
            [friendstableView reloadData];

        }];
        
    }
   
}
-(void)pagingationInAllFriends:(NSString*)urlString
{
    
    NSError * error=nil;
    NSURLResponse * urlReponse=nil;
    urlString=[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *url=[NSURL URLWithString:urlString];
    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
    if (data==nil) {
        NSLog(@" no data");
        return;
    }
    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
    NSLog(@"reponse is %@",response);
    NSMutableArray * tempArray=[[NSMutableArray alloc]initWithArray:self.allFriendsArray];
    [tempArray addObjectsFromArray:[response objectForKey:@"data"]];
    self.allFriendsArray=[NSArray arrayWithArray:tempArray];
    [friendstableView reloadData];
    paginationUrl=[[response objectForKey:@"paging"]objectForKey:@"next"];

}
-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
    
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

//#pragma mark <UICollectionViewDataSource>
//
//- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
//
//    return 1;
//}
//
//
//- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
////    NSLog(@"self  %lu",(unsigned long)self.mutNowArray.count);
//    
//  
//    if (self.segmentControl.selectedSegmentIndex==0) {
//          return self.mutNowArray.count;
//    }else{
//        return self.allFriendsArray.count;
//    }
//    
//    
//}
//

//-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
//    NSDictionary *   dic;
//    if (self.segmentControl.selectedSegmentIndex==0) {
//       dic=[self.mutNowArray objectAtIndex:indexPath.row];
//        
//    }
//    
//    ProfileViewController *profile=[[ProfileViewController alloc] init];
//    profile.FBid=[dic objectForKey:@"id"];
//    
//    [self.navigationController pushViewController:profile animated:YES];
//}

//- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
//    ImageViewCustomCell *cell = (ImageViewCustomCell *)[collectionView dequeueReusableCellWithReuseIdentifier:reuseIdentifier forIndexPath:indexPath];
//    // cell.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"name.png"]];
//    cell.backgroundColor=[UIColor blackColor];
//    NSDictionary *dic;
//    NSString *strUrl;
//    if (self.segmentControl.selectedSegmentIndex==0)
//    {
//        dic=[self.mutNowArray objectAtIndex:indexPath.row];
//        strUrl=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",[dic objectForKey:@"id"]];
//    }
//    else
//    {
//        dic=[self.allFriendsArray objectAtIndex:indexPath.row];
//        strUrl=[[[dic objectForKey:@"picture"]objectForKey:@"data"]objectForKey:@"url"];
//    }
////     NSDictionary *dic=[self.mutNowArray objectAtIndex:indexPath.row];
//   
//    NSURL *url=[NSURL URLWithString:strUrl];
//   
//    [cell.imageView setImageWithURL:url placeholderImage:[UIImage imageNamed:@""]];
//    
//    NSString* nameStr = [dic objectForKey:@"name"];
//    
//    NSArray* firstLastStrings = [nameStr componentsSeparatedByString:@" "];
//    
//    NSString* firstName = [firstLastStrings objectAtIndex:0];
//    NSString* lastName;
//    if(firstLastStrings.count>1)
//    {
//        lastName = [firstLastStrings objectAtIndex:1];
//    }
//    else
//    {
//        lastName=@"";
//    }
//   
//    
//    //    char lastInitialChar = [lastName characterAtIndex:0];
//        //    NSString* newNameStr = [NSString stringWithFormat:@"%@ %c.", firstName, lastInitialChar]
//    cell.textLabel.text = [NSString stringWithFormat:@"%@\n%@",firstName,lastName];
//    return cell;
//}
//
#pragma mark <UICollectionViewDelegate>

/*
// Uncomment this method to specify if the specified item should be highlighted during tracking
- (BOOL)collectionView:(UICollectionView *)collectionView shouldHighlightItemAtIndexPath:(NSIndexPath *)indexPath {
	return YES;
}
*/

/*
// Uncomment this method to specify if the specified item should be selected
- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    return YES;
}
*/

/*
// Uncomment these methods to specify if an action menu should be displayed for the specified item, and react to actions performed on the item
- (BOOL)collectionView:(UICollectionView *)collectionView shouldShowMenuForItemAtIndexPath:(NSIndexPath *)indexPath {
	return NO;
}

- (BOOL)collectionView:(UICollectionView *)collectionView canPerformAction:(SEL)action forItemAtIndexPath:(NSIndexPath *)indexPath withSender:(id)sender {
	return NO;
}

- (void)collectionView:(UICollectionView *)collectionView performAction:(SEL)action forItemAtIndexPath:(NSIndexPath *)indexPath withSender:(id)sender {
	
}
*/

@end
