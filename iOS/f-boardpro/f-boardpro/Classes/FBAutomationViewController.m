//
//  FBAutomationViewController.m
//  SwitchUserSample
//
//  Created by Socioboard 1 on 4/29/15.
//
//

#import "FBAutomationViewController.h"
#import "AppDelegate.h"
#import "MBProgressHUD.h"
#import "ProfileViewController.h"
#import "SUCache.h"
#import "UIImageView+WebCache.h"
#import "ImageViewCustomCell.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import "SingletonClass.h"

@interface FBAutomationViewController ()

@end

@implementation FBAutomationViewController

static NSString * const reuseIdentifier = @"Cell";

- (void)viewDidLoad {
    [super viewDidLoad];
    

    
    self.view.backgroundColor=[UIColor grayColor];
    
    self.refreshControl = [[UIRefreshControl alloc] init];
    [self.refreshControl addTarget:self action:@selector(refershControlAction) forControlEvents:UIControlEventValueChanged];
    [self.collectionView addSubview:self.refreshControl];
    
    NSArray *itemArray=[NSArray arrayWithObjects:@"Friends List",@"Unfriend friends", nil];
    self.segmentControl = [[UISegmentedControl alloc] initWithItems:itemArray];
    self.segmentControl.frame =CGRectMake(-4,5,self.view.frame.size.width,30);
    [self.segmentControl addTarget:self action:@selector(mySegmentControlAction:) forControlEvents: UIControlEventValueChanged];
    [self.segmentControl setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor blackColor]} forState:UIControlStateNormal];
    [self.segmentControl setBackgroundColor:[UIColor whiteColor]];
    [self.segmentControl setTintColor:[UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f]];
    self.segmentControl.selectedSegmentIndex=0;
    [self.view addSubview:self.segmentControl];

    // Uncomment the following line to preserve selection between presentations
    // self.clearsSelectionOnViewWillAppear = NO;
//    UICollectionViewFlowLayout *layout=[[UICollectionViewFlowLayout alloc] init];
//    [layout setScrollDirection:UICollectionViewScrollDirectionVertical];
//    [layout setItemSize:CGSizeMake(125, 40)];
//    
//    [self.collectionView setCollectionViewLayout:layout];
    // Register cell classes
    self.collectionView.frame= CGRectMake(0, 50, self.view.frame.size.width, self.view.frame.size.height-50);
    self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
    self.collectionView.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
    [self.collectionView registerClass:[ImageViewCustomCell class] forCellWithReuseIdentifier:reuseIdentifier];
   
    // Do any additional setup after loading the view.
}

-(void)refershControlAction{


}

-(void)viewWillAppear:(BOOL)animated{
    self.mutOldArray=[[NSMutableArray alloc] init];
    self.mutNowArray=[[NSMutableArray alloc] init];
    [self FetchFreinds];
    self.navigationController.navigationBarHidden=YES;
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(FetchFreinds) name:@"CurrentUserChangedNotification" object:nil];

}

-(void)mySegmentControlAction:(UISegmentedControl *)seg{

    [self.collectionView reloadData];
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

-(void)FetchFreinds{
    if (self.mutOldArray&&self.mutNowArray) {
        self.mutOldArray=[[NSMutableArray alloc] init];
        self.mutNowArray=[[NSMutableArray alloc] init];
    }
    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
        //    NSLog(@"index path %d",index.section+index.row);
    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
    if (token) {
        
    
    [FBSDKAccessToken setCurrentAccessToken:token];
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/friends" parameters:nil];
    
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

#pragma mark <UICollectionViewDataSource>

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {

    return 1;
}


- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
//    NSLog(@"self  %lu",(unsigned long)self.mutNowArray.count);
    
  
    if (self.segmentControl.selectedSegmentIndex==0) {
          return self.mutNowArray.count;
    }else{
        return self.unfriendArray.count;
    }
    
    
}


-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    NSDictionary *   dic;
    if (self.segmentControl.selectedSegmentIndex==0) {
       dic=[self.mutNowArray objectAtIndex:indexPath.row];
        
    }
    
    ProfileViewController *profile=[[ProfileViewController alloc] init];
    profile.FBid=[dic objectForKey:@"id"];
    
    [self.navigationController pushViewController:profile animated:YES];
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    ImageViewCustomCell *cell = (ImageViewCustomCell *)[collectionView dequeueReusableCellWithReuseIdentifier:reuseIdentifier forIndexPath:indexPath];
     cell.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"name.png"]];
    NSDictionary *dic;
    if (self.segmentControl.selectedSegmentIndex==0) {
        dic=[self.mutNowArray objectAtIndex:indexPath.row];

    }else{
        dic=[self.unfriendArray objectAtIndex:indexPath.row];

    }
//     NSDictionary *dic=[self.mutNowArray objectAtIndex:indexPath.row];
    NSString *str=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",[dic objectForKey:@"id"]];
    NSURL *url=[NSURL URLWithString:str];
   
    [cell.imageView setImageWithURL:url placeholderImage:[UIImage imageNamed:@""]];
    
    NSString* nameStr = [dic objectForKey:@"name"];
    NSArray* firstLastStrings = [nameStr componentsSeparatedByString:@" "];
    NSString* firstName = [firstLastStrings objectAtIndex:0];
    NSString* lastName = [firstLastStrings objectAtIndex:1];
        //    char lastInitialChar = [lastName characterAtIndex:0];
        //    NSString* newNameStr = [NSString stringWithFormat:@"%@ %c.", firstName, lastInitialChar]
    cell.textLabel.text = [NSString stringWithFormat:@"%@\n%@",firstName,lastName];
    return cell;
}

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
