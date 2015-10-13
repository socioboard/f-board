//
//  ProfileViewController.m
//  SwitchUserSample
//
//  Created by GBS-ios on 4/25/15.
//
//

#import "ProfileViewControllerFboard.h"
#import "SUCacheFboard.h"
#import "UIImageView+WebCache.h"
#import "SingletonClass.h"
#import "SingletonClass.h"
#import "TableCustomCellFboard.h"
#import <QuartzCore/QuartzCore.h>
#import "ImageViewCustomCellFboard.h"
#define Screen_Width [UIScreen mainScreen].bounds.size.width
#define Screen_Height [UIScreen mainScreen].bounds.size.height




@interface ProfileViewControllerFboard ()
@end

@implementation ProfileViewControllerFboard
@synthesize pictureArray,profileImage;


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
}
-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:YES];
    self.navigationController.navigationBarHidden=YES;
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchMyProfileDetails) name:@"CurrentUserChangedNotification" object:nil];
    [self fetchMyDetails];
    
    [self fetchMyProfileDetails];
    
    [self createFollowTable];
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self fetchMyDetails];
    
    [self fetchMyProfileDetails];

    
     [self createFollowTable];
    
    
    self.view.backgroundColor=[UIColor colorWithRed:(CGFloat)220/255 green:(CGFloat)220/255 blue:(CGFloat)220/255 alpha:1];
    
    
    self.photoIdArray = [[NSMutableArray alloc]init];
    self.pictureArray = [[NSMutableArray alloc]init];
    
    
    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
    // self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
    
    // Do any additional setup after loading the view.
}

-(void)reloadDetails:(NSNotificationCenter *)defaultCenter
{
    
    
    [self fetchMyDetails];
    
    [self fetchMyProfileDetails];
    
    
     [self createFollowTable];
    
}

-(void)fetchMyProfileDetails
{
    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPath.row+indexPath.section].token;
    NSString *str=[NSString stringWithFormat:@"/%@?fields=id,name,work,location,hometown,albums,birthday,cover",@"me"];
    if (token) {
        
        
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:str parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            NSLog(@"result is %@",result);
            
            self.locationString =[[result objectForKey:@"location"]objectForKey:@"name"];
            
            NSLog(@"self.coverstring %@",self.coverString);
            
            //  [self.coverImageview setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-xlf1/v/t1.0-9/s720x720/11960142_122570654759302_2499999453546494909_n.jpg?oh=580d2abcc9243041539190a347a8f775&oe=566441AB&__gda__=1448949036_4de57c89f840011a304296fc65fb6b72"]]];
            
            
            
            // [self.profileImageView setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/me/picture?type=small"] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]]];
            self.coverString = [[result objectForKey:@"cover"]objectForKey:@"source"];
            
            // self.coverImageview.=[FBSDKAccessToken currentAccessToken].userID;
            
            self.profileImageView.profileID=[FBSDKAccessToken currentAccessToken].userID;
            
            
            
            
            NSLog(@"token String %@",[FBSDKAccessToken currentAccessToken].tokenString);
            for (NSDictionary *dic in [result objectForKey:@"work"] ) {
                self.workString = [[dic objectForKey:@"employer"]objectForKey:@"name"];
            }
            
            
            
            self.homeTownString =[[result objectForKey:@"hometown"]objectForKey:@"name"];
            
            
            
            self.nameString =[result objectForKey:@"name"];
            self.nameLabel.text=self.nameString;
            
            self.birthdayString = [result objectForKey:@"birthday"];
            
            
            
            
        }];
        //[self getUrlOfPhotos];
        [self createFollowTable];
        
    }
    
    
    
    
}


-(void)cancelButtonTap:(UIButton *)button{
    self.imageView.hidden=YES;
    self.picimageview.hidden=YES;
    self.theScrollView.hidden=YES;
    self.profileImage.hidden=YES;
    self.cancelButton.hidden=YES;
    
    
}

-(void)createFollowTable
{
    
    UIView * headerView=[[UIView alloc]init];
    headerView.frame=CGRectMake(0, 0, SCREEN_WIDTH,150);
    
    
    //  UIView * headerView1=[[UIView alloc]init];
    //  headerView1.frame=CGRectMake(0, 0, 150,150);
    
    footerview =[[UIView alloc]init];
    footerview.frame=CGRectMake(0,0, SCREEN_WIDTH, 70);
    footerview.backgroundColor=[UIColor whiteColor];
    if (IS_IPHONE_6P)
    {
        profileTableView=[[UITableView alloc]initWithFrame:CGRectMake(0,0, self.view.frame.size.width,self.view.frame.size.height) style:UITableViewStylePlain];
        
    }
    else
    {
        profileTableView=[[UITableView alloc]initWithFrame:CGRectMake(5,0, SCREEN_WIDTH-10,self.view.frame.size.height) style:UITableViewStylePlain];
    }
    profileTableView.dataSource=self;
    profileTableView.delegate=self;
    profileTableView.backgroundColor=[UIColor colorWithRed:(CGFloat)220/255 green:(CGFloat)220/255 blue:(CGFloat)220/255 alpha:1];
    
    profileTableView.separatorStyle=UITableViewCellSeparatorStyleSingleLine;
    // profileTableView.scrollEnabled=NO;
    profileTableView.tableHeaderView=headerView;
    // profileTableView.tableHeaderView=headerView1;
    profileTableView.tableFooterView=footerview;
    [self.view addSubview:profileTableView];
    
    
    
    //
    self.coverImageview =[[UIImageView alloc]init];
    self.coverImageview.frame=CGRectMake(0, 0, headerView.frame.size.width, headerView.frame.size.height);
    [self.coverImageview.layer setBorderColor: [[UIColor grayColor] CGColor]];
    
    [self.coverImageview.layer setBorderWidth:2];
    
    
    [headerView addSubview:self.coverImageview];
    
    NSLog(@"image url %@",self.coverString);
    if(self.coverString)
    {
        NSURL * coverImageUrl=[[NSURL alloc]initWithString:self.coverString];
        [self.coverImageview setImageWithURL:coverImageUrl placeholderImage:[UIImage imageNamed:@"cover"]];
    }
    else
    {
        
    }
    
    //
    self.profileImageView=[[FBSDKProfilePictureView alloc]init];
    self.profileImageView.userInteractionEnabled = YES;
    self.profileImageView.backgroundColor=[UIColor blackColor];
    self.profileImageView.frame=CGRectMake(0,self.coverImageview.frame.size.height-60,60,60);
//    self.profileImageView.layer.cornerRadius=15;
    self.profileImageView.contentMode=UIViewContentModeScaleAspectFill;
    
    [self.coverImageview addSubview:self.profileImageView];
    
    
    
    
    
    
    
    self.nameLabel = [[UILabel alloc]init];
    //    self.nameLabel.text = @"Vinayaka";
    self.nameLabel.numberOfLines = 2;
    self.nameLabel.font=[UIFont fontWithName:@"Helvetica-Bold" size:15];
    self.nameLabel.frame = CGRectMake(80,headerView.frame.size.height-50, 200, 30);
    self.nameLabel.textColor=[UIColor whiteColor];
    [headerView addSubview:self.nameLabel];
    
    
    //
    
    
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
    
}


-(void)fetchMyDetails
{
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    self.pictureArray=[[NSMutableArray alloc]init];
    self.photoIdArray=[[NSMutableArray alloc] init];
    
    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        NSString *str=[NSString stringWithFormat:@"/%@/albums",@"me"];
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:str parameters:nil];
        
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            //            NSLog(@"result is %@",result);
            
            NSString *str1 = [result objectForKey:@"id"];
            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"id"];
                NSString *str2 = [dic objectForKey:@"count"];
                NSMutableDictionary * albumDetail=[[NSMutableDictionary alloc]init];
                if (str1)
                {
                    if([dic objectForKey:@"cover_photo"])
                    {
                        [albumDetail setObject:[dic objectForKey:@"cover_photo"] forKey:@"cover_photo"];
                    }
                    else
                    {
                        [albumDetail setObject:[dic objectForKey:@"id"] forKey:@"cover_photo"];
                        
                    }
                    [albumDetail setObject:[dic objectForKey:@"name"] forKey:@"Name"];
                    [albumDetail setObject:[dic objectForKey:@"id"] forKey:@"ID"];
                   
                    [self.photoIdArray addObject:albumDetail];
                }
                
                if(str2)
                {
                    [albumDetail setObject:[dic objectForKey:@"count"] forKey:@"count"];
                }else
                {
                    [albumDetail setObject:[dic objectForKey:@"count"] forKey:@"count"];
                    
                }
                [albumDetail setObject:[dic objectForKey:@"id"] forKey:@"ID"];
                [self.countArray addObject:albumDetail];
            }
            [profileTableView reloadData];
            //[self getUrlOfPhotos];
        }];
    }
    
}


-(void)getUrlOfPhotos:(NSDictionary*)albumId
{
    [self.pictureArray removeAllObjects];
    NSIndexPath *indexPath= [SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCacheFboard itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
//        for (int i=0; i<self.photoIdArray.count; i++) {
//            
            //        if (self.photoIdArray.count>1) {
            
            NSString *str = [albumId objectForKey:@"ID"];
            
            NSString *checkStr = [NSString stringWithFormat:@"%@/photos",str];
            
            FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:checkStr parameters:nil];
            
            
            [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
                //            NSLog(@"result is======== %@",result);
                
                for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                    NSString *str1 = [dic objectForKey:@"source"];
                    
                    if (str1)
                    {
                        [self.pictureArray addObject:str1];
                    }
                    
                }
                //            NSLog(@"photo id array is %lu & array is ",(unsigned long)self.pictureArray.count);
                //            NSLog(@"photo id array is %@ ",self.pictureArray);
             //   [self createCollectionView];
                [self setImagesInAlbum];
                
            }];
            
            // }
            
        }
    //}
    
}

#pragma mark tableview delegates
#pragma mark ----------

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display";
    
   UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:nil];
    
    
    
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                      reuseIdentifier:CellIdentifier] ;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
     
    }
    
    [cell.layer setBorderColor: [[UIColor grayColor] CGColor]];
    [cell.layer setBorderWidth:1];
    
    
    
    // cell.backgroundColor=[UIColor blueColor];
    
    //cell.textLabel.font = [UIFont fontWithName:@"Menlo-BoldItalic" size:12];
    
    // cell.textLabel.font=[UIFont systemFontSize:12.0f];
    [cell.textLabel setFont:[UIFont boldSystemFontOfSize:12]];
    
    
    
    
    
    
    
    
    if (indexPath.section ==0) {
        cell.textLabel.text = [NSString stringWithFormat:@"Works at %@",self.workString];
        cell.imageView.image = [UIImage imageNamed:@"business_icon.png"];
        cell.layer.cornerRadius=5;
    }
    
    if (indexPath.section ==1) {
        cell.textLabel.text =  [NSString stringWithFormat:@"Lives at %@",self.locationString];
        //cell.imageView.image = [UIImage imageNamed:@"place.png"];
        
        cell.imageView.image = [UIImage imageNamed:@"location_icon.png"];
        cell.layer.cornerRadius=5;
    }
    
    if (indexPath.section ==2) {
        cell.textLabel.text =   [NSString stringWithFormat:@"Hometown  %@",self.homeTownString];
        cell.imageView.image = [UIImage imageNamed:@"home.png"];
        cell.layer.cornerRadius=5;
    }
    
    if (indexPath.section ==3) {
        cell.textLabel.text = [NSString stringWithFormat:@"Birthday  %@",self.birthdayString];
        cell.imageView.image = [UIImage imageNamed:@"birthday_icon"];
        cell.layer.cornerRadius=5;
    }
    
    if(indexPath.section>=4)
    {
        cell.backgroundColor=[UIColor colorWithRed:(CGFloat)0/255 green:(CGFloat)116/225 blue:(CGFloat)104/255 alpha:1];
        
       
       
        self.coverImageview =[[UIImageView alloc]init];
        self.coverImageview.frame=CGRectMake(10, 10, 80,80);
        
        
        
        [cell addSubview:self.coverImageview];
        
        
        
//        [SingletonClass sharedState].accessTokenString=@"CAANGZCSfBfk0BAKKH7uI34RmtocYci7iwqfYSSXdtooJ1JjjZC6e8ZBHWESordn1IA9XR97ZBlbzj4jsAgsuJOYiKQxHXxIN4bXGPOJwwJBApEzHO61rqhVxL048iZBOkiKGrl4xikNxJAwoEPgSLIvsiW0v4UlurbMGAZC7chmIp8XtNHVcvq";
        NSDictionary * albumDetails= [self.photoIdArray objectAtIndex:indexPath.section-4];
        NSString * urlString=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=album&access_token=%@",[albumDetails objectForKey:@"cover_photo"],[SingletonClass sharedState].accessTokenString];
        NSURL * coverImageUrl=[[NSURL alloc]initWithString:urlString];
        [self.coverImageview setImageWithURL:coverImageUrl placeholderImage:[UIImage imageNamed:@"cover"]];
        
        UILabel * albumName=[[UILabel alloc]init];
        albumName.frame=CGRectMake(110, 20,200 ,20);
        albumName.text=[albumDetails objectForKey:@"Name"];
        albumName.font =[UIFont systemFontOfSize:10];
        albumName.textColor=[UIColor blackColor];
        [cell.contentView addSubview:albumName];
        
    }
    return cell;
    
}










-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
       if(indexPath.section>=4)
    {
    [self getUrlOfPhotos:[self.photoIdArray objectAtIndex:indexPath.section-4]];
    }
    
    if(indexPath.section==5)
    {
        
    }
}





-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return 4+self.photoIdArray.count;
}

-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if(indexPath.section>=4)
    {
        return 100;
    }
    else
        return 40;
}
-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    
    return 2.5;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 2.5;
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    UIView * view =[[UIView alloc]initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, 20)];
    view.backgroundColor = [UIColor colorWithRed:(CGFloat)220/255 green:(CGFloat)220/255 blue:(CGFloat)220/255 alpha:1];
    return  view;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    UIView * view =[[UIView alloc]initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, 20)];
    view.backgroundColor = [UIColor colorWithRed:(CGFloat)220/255 green:(CGFloat)220/255 blue:(CGFloat)220/255 alpha:1];
    
    return  view;
}
#pragma mark SetImages In Profile
-(void)setImagesInAlbum
{
    NSLog(@"Picture Array========== %@",self.pictureArray);
    self.theScrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0,0, self.view.frame.size.width, self.view.frame.size.height)];
    self.theScrollView.delegate = self;
    self.theScrollView.pagingEnabled = YES;
    self.theScrollView.scrollEnabled=YES;
    self.theScrollView.backgroundColor=[UIColor blackColor];
    
    self.theScrollView.contentSize=CGSizeMake(Screen_Width*self.pictureArray.count, 100);
    self.theScrollView.delegate=self;
    
    [self setImageInScrollView];
    self.theScrollView.showsHorizontalScrollIndicator = YES;
    self.theScrollView.showsVerticalScrollIndicator = NO;
    [self.view addSubview :self.theScrollView];
    
    
    
    self.cancelButton=[UIButton buttonWithType:UIButtonTypeCustom];
    [self.cancelButton addTarget:self action:@selector(cancelButtonTap:) forControlEvents:UIControlEventTouchUpInside];
    self.cancelButton.frame=CGRectMake(SCREEN_WIDTH-50, 20, 20, 20);
    [self.cancelButton setImage:[UIImage imageNamed:@"close.png"] forState:UIControlStateNormal];
    [self.view addSubview:self.cancelButton];

}
-(void)setImageInScrollView
{
    
    
    //    frame.origin.x=sel
    
    for (int i=0; i<pictureArray.count; i++) {
        
        CGRect frame;
        frame.origin.x=self.theScrollView.frame.size.width*i;
        frame.origin.y=0;
        frame.size=self.theScrollView.frame.size;
        
        profileImage=[[UIImageView alloc]initWithFrame:frame];
        [profileImage sizeThatFits:CGSizeMake(self.theScrollView.frame.size.width, self.theScrollView.frame.size.height)];
        //        profileImage.image=[UIImage imageNamed:[NSString stringWithFormat:@"%@",[pictureArray objectAtIndex:i]]];
        profileImage.contentMode=UIViewContentModeScaleAspectFit;
        [profileImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@",[pictureArray objectAtIndex:i]]]];
        
        [self.theScrollView addSubview:profileImage];
        
        
        
        
    }
    
    
    
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
